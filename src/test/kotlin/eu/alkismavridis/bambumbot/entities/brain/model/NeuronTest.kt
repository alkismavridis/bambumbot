package eu.alkismavridis.bambumbot.entities.brain.model

import eu.alkismavridis.bambumbot.entities.brain.activators.LinearActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.activators.ReluActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.activators.SigmoidActivationFunction
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test

internal class NeuronTest {
    @Test
    fun reset_shouldSetInputToZero() {
        val neuron = Neuron(SigmoidActivationFunction())
        neuron.input = 5.0

        neuron.reset()
        assertThat(neuron.input).isEqualTo(0.0, Offset.offset(0.000001))
    }

    @Test
    fun fire_shouldAddToNextNeurons() {
        // Create a small network
        val neuron1a = Neuron(SigmoidActivationFunction())
        val neuron1b = Neuron(LinearActivationFunction())
        val neuron2a = Neuron(LinearActivationFunction())
        val neuron2b = Neuron(LinearActivationFunction())
        neuron1a.addSynapseTo(neuron2a)
        neuron1a.addSynapseTo(neuron2b)
        neuron1b.addSynapseTo(neuron2b)

        // prepare weights and outputs
        neuron1a.output = 2.0
        neuron1b.output = 5.0
        neuron1a.synapses[0].set(3.0)
        neuron1a.synapses[1].set(4.0)
        neuron1b.synapses[0].set(1.5)

        // fire neurons
        neuron1a.fire()
        assertThat(neuron2a.input).isEqualTo(6.0)
        assertThat(neuron2b.input).isEqualTo(8.0)

        neuron1b.fire()
        assertThat(neuron2a.input).isEqualTo(6.0)
        assertThat(neuron2b.input).isEqualTo(8.0 + 7.5)
    }


    /// ACTIVATION TESTS
    @Test
    fun activate_shouldActivateWithLinearFunction() {
        val neuron = Neuron(LinearActivationFunction())
        neuron.input = 2.0

        neuron.activate()
        assertThat(neuron.output).isEqualTo(2.0)
    }

    @Test
    fun activate_shouldActivateWithSigmoidFunction() {
        val neuron = Neuron(SigmoidActivationFunction())
        neuron.input = 2.0

        neuron.activate()
        assertThat(neuron.output).isEqualTo(0.880797, Offset.offset(0.00001))
    }


    @Test
    fun sactivate_houldActivateWithReluFunction() {
        val neuron = Neuron(ReluActivationFunction())

        neuron.input = 2.0
        neuron.activate()
        assertThat(neuron.output).isEqualTo(2.0)

        neuron.input = -2.0
        neuron.activate()
        assertThat(neuron.output).isEqualTo(0.0)
    }

    @Test
    fun calculateErrorDerivativeTest() {
        val neuron = Neuron(SigmoidActivationFunction())

        val targetNeuron1 = Neuron(SigmoidActivationFunction()).apply { this.delta = 1.7 }
        val targetNeuron2 = Neuron(SigmoidActivationFunction()).apply { this.delta = 2.1 }
        val targetNeuron3 = Neuron(SigmoidActivationFunction()).apply { this.delta = -0.4 }

        neuron.addSynapseTo(targetNeuron1)
        neuron.addSynapseTo(targetNeuron2)
        neuron.addSynapseTo(targetNeuron3)

        neuron.synapses[0].set(1.2)
        neuron.synapses[1].set(-4.5)
        neuron.synapses[2].set(2.3)

        neuron.calculateErrorDerivative()
        assertThat(neuron.errorDerivative).isEqualTo(1.7*1.2 - 2.1*4.5 - 0.4*2.3, Offset.offset(0.00001))
    }

    @Test
    fun calculateDelta() {
        val neuron = Neuron(SigmoidActivationFunction())
        neuron.input = 5.5
        neuron.errorDerivative = 2.1

        neuron.calculateDelta()
        assertThat(neuron.delta).isEqualTo(0.00405 * 2.1, Offset.offset(0.0001))
    }
}
