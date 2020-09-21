package eu.alkismavridis.bambumbot.entities.brain_state

import eu.alkismavridis.bambumbot.entities.brain.model.NeuralNetwork

class StateLoader {
    companion object {
        fun save(network: NeuralNetwork, array: DoubleArray) {
            var currentIndex = 0
            network.layers.forEach {layer ->
                layer.neurons.forEach {neuron ->
                    neuron.synapses.forEach { array[currentIndex++] = it.weight }
                }

                layer.bias.synapses.forEach { array[currentIndex++] = it.weight }
            }
        }

        fun load(network: NeuralNetwork, array: DoubleArray) {
            var currentIndex = 0
            network.layers.forEach {layer ->
                layer.neurons.forEach {neuron ->
                    neuron.synapses.forEach { it.set(array[currentIndex++]) }
                }

                layer.bias.synapses.forEach { it.set(array[currentIndex++]) }
            }
        }

        fun randomize(array: DoubleArray, range: Double) {
            for (i in array.indices) {
                array[i] = (Math.random() - 0.5) * range
            }
        }
    }
}



