package eu.alkismavridis.bambumbot.entities.brain.model

import eu.alkismavridis.bambumbot.entities.brain.activators.ActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.activators.LinearActivationFunction

class NetworkLayer {
    var neurons = mutableListOf<Neuron>()
    internal val bias: Neuron


    /// CONSTRUCTORS
    constructor() {
        this.bias = Neuron(LinearActivationFunction()).apply { this.output = 1.0 }
    }

    constructor(bias:Neuron) {
        this.bias = bias
    }

    constructor(neuronCount: Int, activationFunc: ActivationFunction) {
        this.bias = Neuron(LinearActivationFunction()).apply { this.output = 1.0 }
        for (i in 0 until neuronCount) {
            this.addNeuron(Neuron(activationFunc))
        }
    }

    fun addNeuron(neuron:Neuron) {
        this.neurons.add(neuron)
    }


    /// GETTERS
    fun getSynapseCount() : Int {
        var result = 0
        this.neurons.forEach { result += it.synapses.size }
        result += this.bias.synapses.size

        return result
    }


    /// FIRING
    fun reset() {
        for (neuron in this.neurons) {
            neuron.reset()
        }
    }

    fun fire() {
        for (neuron in this.neurons) {
            neuron.fire()
        }

        this.bias.fire()
    }

    fun activate() {
        for (neuron in this.neurons) {
            neuron.activate()
        }
    }
}
