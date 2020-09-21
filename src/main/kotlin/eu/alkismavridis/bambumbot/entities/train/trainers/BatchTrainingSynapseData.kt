package eu.alkismavridis.bambumbot.entities.train.trainers

import eu.alkismavridis.bambumbot.entities.brain.model.NeuronSynapse

class BatchTrainingSynapseData(val synapse: NeuronSynapse) {
    var offset = 0.0; private set

    fun addToOffset() {
        this.offset += (this.synapse.source.output * this.synapse.target.delta)
    }

    fun resetOffset() {
        this.offset = 0.0
    }
}
