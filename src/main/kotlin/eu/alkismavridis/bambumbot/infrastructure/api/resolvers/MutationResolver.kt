package eu.alkismavridis.bambumbot.infrastructure.api.resolvers

import eu.alkismavridis.bambumbot.entities.BambumRobot
import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.entities.brain_structure.BrainConfig
import eu.alkismavridis.bambumbot.entities.data_provider.DataProviderConfig
import eu.alkismavridis.bambumbot.entities.train.TrainerConfig
import eu.alkismavridis.bambumbot.entities.train.TrainingSessionRepository
import eu.alkismavridis.bambumbot.infrastructure.providers.DataProviderFactory
import eu.alkismavridis.bambumbot.usecases.*
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.lang.IllegalStateException
import java.util.*


@Component
class MutationResolver(
        private val robotRepo: RobotRepository,
        private val trainingSessionRepo: TrainingSessionRepository,
        private val dataProviderFactory: DataProviderFactory
) : GraphQLMutationResolver {

    fun createRobot(name: String, brainConfig: BrainConfig, dataProviderConfig: DataProviderConfig, trainerConfig: TrainerConfig) : BambumRobot {
        return CreateRobot().save(
            name,
            brainConfig,
            dataProviderConfig,
            trainerConfig,
            this.robotRepo
        )
    }

    fun deleteRobot(robotId: UUID) : Boolean {
        DeleteRobot().delete(robotId, this.robotRepo, trainingSessionRepo)
        return true
    }

    fun loadBestState(robotId: UUID) : Boolean {
        LoadBestRobotState().load(robotId, this.robotRepo)
        return true
    }

    fun loadState(robotId: UUID, newState: List<Double>) : Boolean {
        SetRobotState().set(robotId, newState.toDoubleArray(), this.robotRepo)
        return true
    }

    fun startTraining(robotId: UUID) : TrainingData {
        return StartTraining().start(robotId, this.robotRepo, this.trainingSessionRepo, this.dataProviderFactory::create)
    }

    fun stopTraining(robotId: UUID) : TrainingData {
        return StopTraining().stop(robotId, this.robotRepo, this.trainingSessionRepo) ?:
            throw IllegalStateException("Robot $robotId was not training.")
    }
}
