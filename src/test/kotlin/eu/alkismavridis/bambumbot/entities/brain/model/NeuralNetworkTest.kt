package eu.alkismavridis.bambumbot.entities.brain.model

import eu.alkismavridis.bambumbot.entities.brain.activators.LinearActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.activators.SigmoidActivationFunction
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import java.util.stream.Collectors


internal class NeuralNetworkTest {
    @Test
    fun fire_shouldProduceMathematicallyCorrectResult() {
        val network = this.createSimpleNetwork()

        network.fire()
        assertThat(network.layers[1].neurons[0].input).isEqualTo(-0.14, Offset.offset(0.001))
        assertThat(network.layers[1].neurons[0].output).isEqualTo(0.4651, Offset.offset(0.001))
        assertThat(network.layers[1].neurons[1].input).isEqualTo(0.2, Offset.offset(0.001))
        assertThat(network.layers[1].neurons[1].output).isEqualTo(0.5498, Offset.offset(0.001))
        assertThat(network.layers[1].neurons[2].input).isEqualTo(0.86, Offset.offset(0.001))
        assertThat(network.layers[1].neurons[2].output).isEqualTo(0.7027, Offset.offset(0.001))

        assertThat(network.layers[2].neurons[0].input).isEqualTo(-0.1623, Offset.offset(0.001))
        assertThat(network.layers[2].neurons[0].output).isEqualTo(-0.1623, Offset.offset(0.001))
        assertThat(network.layers[2].neurons[1].input).isEqualTo(-0.11609, Offset.offset(0.001))
        assertThat(network.layers[2].neurons[1].output).isEqualTo(-0.11609, Offset.offset(0.001))

        assertThat(network.layers[3].neurons[0].input).isEqualTo(-0.162806, Offset.offset(0.001))
        assertThat(network.layers[3].neurons[0].output).isEqualTo(0.4594, Offset.offset(0.001))
        assertThat(network.layers[3].neurons[1].input).isEqualTo(0.689, Offset.offset(0.001))
        assertThat(network.layers[3].neurons[1].output).isEqualTo(0.6657, Offset.offset(0.001))
    }

    @Test
    fun backPropagateTest() {
        val network = this.createSimpleNetwork()
        network.layers.last().neurons[0].errorDerivative = 2.1
        network.layers.last().neurons[1].errorDerivative = -0.7

        network.fire()
        network.backPropagate()

        this.assertBackPropagationData(network.layers[3].neurons[0], 2.1, 0.521537)
        this.assertBackPropagationData(network.layers[3].neurons[1], -0.7, -0.155783)

        this.assertBackPropagationData(network.layers[2].neurons[0], -0.035897, -0.035897)
        this.assertBackPropagationData(network.layers[2].neurons[1], -0.266187, -0.266187)

        this.assertBackPropagationData(network.layers[1].neurons[0], -0.071186, -0.017709)
        this.assertBackPropagationData(network.layers[1].neurons[1], 0.13878, 0.034351)
        this.assertBackPropagationData(network.layers[1].neurons[2], -0.079856, -0.01668)

        //input layer and bias neurons should not be affected
        this.assertBackPropagationData(network.layers[0].neurons[0], 0.0, 0.0)
        this.assertBackPropagationData(network.layers[0].neurons[1], 0.0, 0.0)

        this.assertBackPropagationData(network.layers[0].bias, 0.0, 0.0)
        this.assertBackPropagationData(network.layers[1].bias, 0.0, 0.0)
        this.assertBackPropagationData(network.layers[2].bias, 0.0, 0.0)
        this.assertBackPropagationData(network.layers[3].bias, 0.0, 0.0)
    }

    @Test
    fun getSynapseStream() {
        val network = this.createSimpleNetwork()

        val synapses = network.getSynapseStream().collect(Collectors.toList())

        assertThat(synapses.size).isEqualTo(network.getSynapseCount())
        network.layers.forEach {layer ->
            layer.neurons.forEach{neuron -> this.containsSynapsesOf(neuron, synapses)}
            this.containsSynapsesOf(layer.bias, synapses)
        }
    }



    /// UTILS
    private fun createSimpleNetwork() : NeuralNetwork {
        val result = NeuralNetwork()

        result.addLayer(NetworkLayer(2, LinearActivationFunction()))
        result.addLayer(NetworkLayer(3, SigmoidActivationFunction()))
        result.addLayer(NetworkLayer(2, LinearActivationFunction()))
        result.addLayer(NetworkLayer(2, SigmoidActivationFunction()))

        //connect the layers
        this.connectNeuronTo(result.layers[0].neurons[0], result.layers[1], arrayOf(0, 2))
        this.connectNeuronTo(result.layers[0].neurons[1], result.layers[1], arrayOf(0, 1, 2))
        this.connectNeuronTo(result.layers[0].bias, result.layers[1], arrayOf(0, 1, 2))

        this.connectNeuronTo(result.layers[1].neurons[0], result.layers[2], arrayOf(0, 1))
        this.connectNeuronTo(result.layers[1].neurons[1], result.layers[2], arrayOf(0, 1))
        this.connectNeuronTo(result.layers[1].neurons[2], result.layers[2], arrayOf(1))
        this.connectNeuronTo(result.layers[1].bias, result.layers[2], arrayOf(0, 1))

        this.connectNeuronTo(result.layers[2].neurons[0], result.layers[3], arrayOf(0, 1))
        this.connectNeuronTo(result.layers[2].neurons[1], result.layers[3], arrayOf(0, 1))
        this.connectNeuronTo(result.layers[2].bias, result.layers[3], arrayOf(0, 1))


        //dummy input
        val inputLayer = result.layers[0]
        inputLayer.neurons[0].output = 0.4
        inputLayer.neurons[1].output = 0.6

        //set weights of inputLayer
        inputLayer.neurons[0].synapses[0].set(0.1)
        inputLayer.neurons[0].synapses[1].set(-0.25)

        inputLayer.neurons[1].synapses[0].set(0.2)
        inputLayer.neurons[1].synapses[1].set(0.5)
        inputLayer.neurons[1].synapses[2].set(0.1)

        inputLayer.bias.synapses[0].set(-0.3)
        inputLayer.bias.synapses[1].set(-0.1)
        inputLayer.bias.synapses[2].set(0.9)


        //adjust weights of first hidden layer 1
        val hidden1 = result.layers[1]
        hidden1.neurons[0].synapses[0].set(0.5)
        hidden1.neurons[0].synapses[1].set(0.2)
        hidden1.neurons[1].synapses[0].set(-0.9)
        hidden1.neurons[1].synapses[1].set(-0.4)
        hidden1.neurons[2].synapses[0].set(0.3)
        hidden1.bias.synapses[0].set(0.1)
        hidden1.bias.synapses[1].set(-0.2)

        //adjust weights of first hidden layer 2
        val hidden2 = result.layers[2]
        hidden2.neurons[0].synapses[0].set(0.2)
        hidden2.neurons[0].synapses[1].set(0.9)
        hidden2.neurons[1].synapses[0].set(-0.6)
        hidden2.neurons[1].synapses[1].set(-0.3)
        hidden2.bias.synapses[0].set(-0.2)
        hidden2.bias.synapses[1].set(0.8)

        return result
    }


    private fun connectNeuronTo(neuron: Neuron, nextLayer:NetworkLayer, nextLayerNeuronIndecies: Array<Int>) {
        for (i in nextLayerNeuronIndecies) {
            neuron.addSynapseTo(nextLayer.neurons[i])
        }
    }

    private fun containsSynapsesOf(neuron: Neuron, list: List<NeuronSynapse>) {
        neuron.synapses.forEach {
            assertThat(list).contains(it)
        }
    }

    private fun assertBackPropagationData(neuron: Neuron, expectedErrorDerivative: Double, expectedDelta: Double) {
        assertThat(neuron.errorDerivative).isEqualTo(expectedErrorDerivative, Offset.offset(0.0001))
        assertThat(neuron.delta).isEqualTo(expectedDelta, Offset.offset(0.0001))
    }
}
