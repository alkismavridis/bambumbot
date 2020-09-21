package eu.alkismavridis.bambumbot.usecases

import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.entities.train.TrainingSessionRepository
import eu.alkismavridis.bambumbot.usecases.internal.RequireRobot
import org.slf4j.LoggerFactory
import java.util.*


class StopTraining {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun stop(robotId: UUID, robotRepository: RobotRepository, trainingSessionRepository: TrainingSessionRepository) : TrainingData? {
        val robot = RequireRobot().require(robotId, robotRepository)
        val trainingSession = trainingSessionRepository.delete(robotId)

        if (trainingSession == null) {
            log.info("Robot {} was not training. Will not stop it.", robotId)
            return null
        }

        trainingSession.isTraining = false
        log.info("Robot {} stopped training.", robotId)

        if (trainingSession.minimumError < trainingSession.errorBeforeTraining ) {
            log.info("Best state found. Updating robot {}", robotId)
            for (i in robot.bestState.indices) {
                val minErrorState = trainingSession.minimumErrorState[i]
                robot.bestState[i] = minErrorState
                robot.currentState[i] = minErrorState
            }

            robot.leastError = trainingSession.minimumError
            robotRepository.save(robot)
            log.info("Robot {} successfully updated", robotId)
        }

        return TrainingData(trainingSession.errorBeforeTraining, trainingSession.minimumError, trainingSession.minimumErrorState)
    }
}
