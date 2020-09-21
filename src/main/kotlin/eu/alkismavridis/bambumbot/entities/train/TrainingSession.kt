package eu.alkismavridis.bambumbot.entities.train

import eu.alkismavridis.bambumbot.entities.brain.model.NeuralNetwork
import eu.alkismavridis.bambumbot.entities.brain_state.StateLoader
import java.util.*


class TrainingSession(val robotId: UUID, val errorBeforeTraining:Double, val minimumErrorHandler: (sess: TrainingSession) -> Unit, synapseCount: Int) {
    var minimumError: Double = errorBeforeTraining
    val minimumErrorState = DoubleArray(synapseCount)
    var isTraining = false

    var trainingThread: Thread? = null


    fun updateMinimum(network: NeuralNetwork, newMinimumError: Double) {
        this.minimumError = newMinimumError
        StateLoader.save(network, this.minimumErrorState)
        minimumErrorHandler(this)
    }
}
