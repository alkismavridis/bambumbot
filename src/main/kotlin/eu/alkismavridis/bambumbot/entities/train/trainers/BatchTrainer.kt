package eu.alkismavridis.bambumbot.entities.train.trainers

import eu.alkismavridis.bambumbot.entities.brain.model.NeuralNetwork
import eu.alkismavridis.bambumbot.entities.data_provider.DataProvider
import eu.alkismavridis.bambumbot.entities.train.NetworkTrainer
import eu.alkismavridis.bambumbot.entities.train.TrainingSession
import org.slf4j.LoggerFactory

class BatchTrainer(
    private val batchSize: Int,
    private val learningRate: Double,
    private val momentum: Double
) : NetworkTrainer {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }


    override fun start(network: NeuralNetwork, dataProvider: DataProvider, session: TrainingSession) {
        val trainingState = BatchTrainingState(network, dataProvider, this.learningRate, this.momentum)

        log.info("Starting training")
        session.isTraining = true

        log.trace("Randomizing weights of {}", session.robotId)
        trainingState.randomizeSynapses()

        while (session.isTraining) {
            val didWeGetInput = trainingState.feedInput()
            if (!didWeGetInput) {
                log.debug("Data provider ran out of data. End training.")
                session.isTraining = false
                break
            }

            trainingState.makePrediction()
            trainingState.processFeedback()

            if (trainingState.batchCount >= this.batchSize) {
                trainingState.adjustNetwork(session)
            }
        }

    }
}
