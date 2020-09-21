package eu.alkismavridis.bambumbot.entities.brain_structure

import eu.alkismavridis.bambumbot.entities.brain.activators.*
import eu.alkismavridis.bambumbot.entities.brain.activators.ActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.model.NetworkLayer
import eu.alkismavridis.bambumbot.entities.brain.model.NeuralNetwork
import eu.alkismavridis.bambumbot.entities.brain.model.Neuron
import java.lang.IllegalArgumentException

class StructureUtils {
    companion object {
        fun from(code:String) : ActivationFunction {
            return when(code) {
                "LINEAR" -> LinearActivationFunction()
                "RELU" -> ReluActivationFunction()
                "LEAKY_RELU" -> LeakyReluActivationFunction()
                "SIGMOID" -> SigmoidActivationFunction()
                "TANH" -> TanhActivationFunction()
                else -> throw IllegalArgumentException("Unknown activation function code: \"${code}\"")
            }
        }



        fun initLayers(layerConfig: LayerConfig, network: NeuralNetwork, parentActivationFunction: ActivationFunction) {
            val activationFunction =
                    if (layerConfig.activation == "") parentActivationFunction
                    else from(layerConfig.activation)

            for (i in 0 until layerConfig.times) {
                initSingeLayer(layerConfig, network, activationFunction)
            }
        }

        private fun initSingeLayer(layerConfig: LayerConfig, network: NeuralNetwork, activationFunction: ActivationFunction) {
            val layer = NetworkLayer()
            for (i in 0 until layerConfig.neuronCount) {
                layer.addNeuron(Neuron(activationFunction))
            }

            network.addLayer(layer)
        }

        /// CONNECTING
        internal fun connectLayers(layerConfig: LayerConfig, iterator: LayersIterator) {
            for (i in 0 until layerConfig.times) {
                this.connectSingleLayer(layerConfig, iterator)
            }
        }

        private fun connectSingleLayer(layerConfig: LayerConfig, iterator: LayersIterator) {
            if (iterator.current == null) {
                throw IllegalStateException("Cannot connect layer null")
            }

            when (layerConfig.type) {
                "OUT" -> {
                    if(iterator.next != null) {
                        throw IllegalStateException("Only the last layer can be an output layer")
                    }
                }

                "FULLY_CONNECTED" -> {
                    if(iterator.next == null) {
                        throw IllegalStateException("Last layer must have type: OUT")
                    }

                    iterator.current!!.neurons.forEach { currentNeuron ->
                        iterator.next!!.neurons.forEach{ nextNeuron -> currentNeuron.addSynapseTo(nextNeuron) }
                    }
                }

                else -> throw IllegalStateException("Unknown layer type: ${layerConfig.type}")
            }

            // connect bias with the next layer
            if (iterator.next != null) {
                iterator.next!!.neurons.forEach{ nextNeuron -> iterator.current!!.bias.addSynapseTo(nextNeuron) }
            }

            iterator.progress()
        }

        fun generate(brainConfig: BrainConfig) : NeuralNetwork {
            val result = NeuralNetwork()
            val activationFunction = StructureUtils.from(brainConfig.activation)

            brainConfig.layers.forEach {
                initLayers(it, result, activationFunction)
            }

            val iterator = LayersIterator(result.layers.iterator())
            brainConfig.layers.forEach {
                connectLayers(it, iterator)
            }

            return result
        }

        fun getMetadata(network: NeuralNetwork) : BrainMetadata {
            return BrainMetadata(
                    network.layers.size,
                    network.getNeuronCount(),
                    network.getSynapseCount(),
                    network.layers[0].neurons.size,
                    network.layers.last().neurons.size
            )
        }
    }
}
