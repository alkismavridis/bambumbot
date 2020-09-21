package eu.alkismavridis.bambumbot.usecases

import eu.alkismavridis.bambumbot.entities.BambumRobot
import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.entities.brain.model.NeuralNetwork
import eu.alkismavridis.bambumbot.entities.brain_structure.StructureUtils
import eu.alkismavridis.bambumbot.entities.data_provider.DataProvider
import eu.alkismavridis.bambumbot.entities.data_provider.DataProviderConfig
import eu.alkismavridis.bambumbot.entities.train.TrainerUtils
import eu.alkismavridis.bambumbot.entities.train.TrainingSession
import eu.alkismavridis.bambumbot.entities.train.TrainingSessionRepository
import eu.alkismavridis.bambumbot.usecases.internal.RequireRobot
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import java.util.*

typealias DataProviderGenerator = (conf:DataProviderConfig) -> DataProvider

class StartTraining {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun start(
            robotId: UUID,
            robotRepository: RobotRepository,
            trainingSessionRepository: TrainingSessionRepository,
            dataProviderGenerator: DataProviderGenerator
    ) : TrainingData {
        val robot = RequireRobot().require(robotId, robotRepository)

        val brain = StructureUtils.generate(robot.brainConfig)
        val dataProvider = dataProviderGenerator(robot.dataProviderConfig)
        val trainer = TrainerUtils.generate(robot.trainerConfig)

        this.assertNotTraining(robotId, trainingSessionRepository)
        this.assertBrainCompatibilityWithDataProvider(brain, dataProvider, robotId)

        val newSession = TrainingSession(robotId, robot.leastError, { this.handleMinimumFound(it, robot) }, brain.getSynapseCount())
        newSession.trainingThread = Thread{ trainer.start(brain, dataProvider, newSession) }
        newSession.trainingThread!!.start()
        trainingSessionRepository.save(newSession)

        log.info("Started training for {}", robotId)
        return TrainingData(
                newSession.errorBeforeTraining,
                newSession.minimumError,
                newSession.minimumErrorState
        )
    }

    private fun handleMinimumFound(session: TrainingSession, robot:BambumRobot) {
        log.info("Minimum error {} found for {}", robot.id, session.minimumError)
    }

    private fun assertNotTraining(robotId:UUID, trainingSessionRepository:TrainingSessionRepository) {
        if (trainingSessionRepository.getSessionFor(robotId) != null) {
            throw IllegalArgumentException("Robot $robotId is already training")
        }
    }

    private fun assertBrainCompatibilityWithDataProvider(brain: NeuralNetwork, dataProvider:DataProvider, robotId: UUID) {
        val inputCount = brain.layers[0].neurons.size
        val outputCount = brain.layers.last().neurons.size

        if (inputCount != dataProvider.getInputCount()) {
            throw IllegalArgumentException("Robot $robotId has $inputCount input neurons, but its data provider expected ${dataProvider.getInputCount()}")
        }

        if (outputCount != dataProvider.getOutputCount()) {
            throw IllegalArgumentException("Robot $robotId has $inputCount output neurons, but its data provider expected ${dataProvider.getOutputCount()}")
        }
    }
}
