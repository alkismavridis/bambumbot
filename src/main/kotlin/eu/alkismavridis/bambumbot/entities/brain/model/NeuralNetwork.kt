package eu.alkismavridis.bambumbot.entities.brain.model

import java.lang.IllegalStateException
import java.util.stream.Stream

class NeuralNetwork {
    var layers = mutableListOf<NetworkLayer>()

    fun addLayer(layer: NetworkLayer) {
        this.layers.add(layer)
    }


    /// GETTERS
    fun getNeuronCount() : Int {
        var result = 0
        this.layers.forEach {
            result += it.neurons.size
        }

        return result
    }

    fun getSynapseCount() : Int {
        var result = 0
        this.layers.forEach { layer ->
            layer.neurons.forEach {
                result += it.synapses.size
            }

            result += layer.bias.synapses.size
        }

        return result
    }

    fun getSynapseStream() : Stream<NeuronSynapse> {
        var result = Stream.empty<NeuronSynapse>()
        this.layers.forEach { layer ->
            layer.neurons.forEach { neuron ->
                result = Stream.concat(result, neuron.synapses.stream())
            }

            result = Stream.concat(result, layer.bias.synapses.stream())
        }

        return result
    }

    fun loadInput(input:DoubleArray) {
        val inputNeurons = this.layers[0].neurons
        if (inputNeurons.size != input.size) {
            throw IllegalStateException("Wrong weight size. Expected ${inputNeurons.size} but found ${input.size}")
        }

        for (i in 0 until inputNeurons.size) {
            inputNeurons[i].output = input[i]
        }
    }


    /// FIRING
    fun fire() {
        for (i in 0 until this.layers.size - 1) {
            val current = this.layers[i]
            val next = this.layers[i + 1]

            next.reset()
            current.fire()
            next.activate()
        }
    }


    /// BACK PROPAGATING
    /**
     * Calculates the errorDerivative and Delta of the whole network
     *
     * Please note that:
     * 1. The errorDerivative of the output layer must be set (this will guide the whole process)
     * 2. Bias neurons have neither errorDerivative nor delta.
     * 3. Input layer has neither errorDerivative nor delta
     * */
    fun backPropagate() {
        val layerCount = this.layers.size
        if (layerCount <= 1) return

        // output layer needs only delta
        this.layers.last().neurons.forEach { it.calculateDelta() }

        // each other layer (except input) need both errorDerivative and delta.
        for (i in layerCount - 2 downTo 1) {
            this.layers[i].neurons.forEach {
                it.calculateErrorDerivative()
                it.calculateDelta()
            }
        }
    }

    fun getOutput(): DoubleArray {
        val outLayer = this.layers.last()
        return outLayer.neurons.map { it.output }.toDoubleArray()
    }
}
