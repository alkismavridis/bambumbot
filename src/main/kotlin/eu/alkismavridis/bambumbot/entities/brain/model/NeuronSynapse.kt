package eu.alkismavridis.bambumbot.entities.brain.model

class NeuronSynapse(val source: Neuron, val target: Neuron) {
    var weight: Double = 0.0; private set

    fun set(newWeight: Double) { this.weight = newWeight }

    fun moveBy(offset: Double) {
        this.weight += offset
    }

    fun random(range: Double) {
        this.weight = Math.random() * range
    }
}
