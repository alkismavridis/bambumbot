package eu.alkismavridis.bambumbot.usecases

import eu.alkismavridis.bambumbot.entities.BambumRobot
import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.entities.brain.model.NeuralNetwork
import eu.alkismavridis.bambumbot.entities.brain_state.StateLoader
import eu.alkismavridis.bambumbot.entities.brain_structure.BrainConfig
import eu.alkismavridis.bambumbot.entities.brain_structure.StructureUtils
import eu.alkismavridis.bambumbot.entities.data_provider.DataProviderConfig
import eu.alkismavridis.bambumbot.entities.train.TrainerConfig
import org.slf4j.LoggerFactory
import java.util.*

class CreateRobot {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun save(name: String, brainConfig: BrainConfig, dataProviderConfig: DataProviderConfig, trainerConfig: TrainerConfig, repo: RobotRepository) : BambumRobot {
        val neuralNetwork = StructureUtils.generate(brainConfig)
        val synapseCount = neuralNetwork.getSynapseCount()

        val robot = BambumRobot(
                UUID.randomUUID(),
                name,
                brainConfig,
                StructureUtils.getMetadata(neuralNetwork),
                dataProviderConfig,
                trainerConfig,
                DoubleArray(synapseCount),
                Double.MAX_VALUE,
                DoubleArray(synapseCount)
        )

        StateLoader.randomize(robot.bestState, 5.0)
        StateLoader.randomize(robot.currentState, 5.0)

        validateRobotSetup(robot, neuralNetwork)

        log.debug("Saving robot with id ${robot.id}.")
        repo.save(robot)
        log.info("Robot with id ${robot.id} has ben saved.")

        return robot
    }

    private fun validateRobotSetup(robot: BambumRobot, neuralNetwork: NeuralNetwork) {
        // TODO
    }
}
