package eu.alkismavridis.bambumbot.entities.brain.model

import eu.alkismavridis.bambumbot.entities.brain.activators.ActivationFunction

open class Neuron(internal val activationFunc: ActivationFunction) {
    var synapses = mutableListOf<NeuronSynapse>()
    internal var input: Double = 0.0
    internal var output: Double = 0.0

    // back propagation data
    internal var delta: Double = 0.0
    internal var errorDerivative: Double = 0.0


    /// BUILD UTILS
    fun addSynapseTo(nextNeuron: Neuron) {
        this.synapses.add(NeuronSynapse(this, nextNeuron))
    }


    /// FIRING
    fun reset() {
        this.input = 0.0
    }

    open fun fire() {
        for (syn in this.synapses) {
            syn.target.input += syn.weight * this.output
        }
    }

    open fun activate() {
        this.output = this.activationFunc.apply(this.input)
    }


    /// BACK PROPAGATING
    fun calculateErrorDerivative() {
        var errorDerivative = 0.0
        for (syn in this.synapses) {
            errorDerivative += syn.weight * syn.target.delta
        }

        this.errorDerivative = errorDerivative
    }

    open fun calculateDelta() {
        this.delta = this.activationFunc.applyDerivative(this.input) * this.errorDerivative
    }
}
