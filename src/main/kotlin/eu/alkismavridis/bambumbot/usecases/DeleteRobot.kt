package eu.alkismavridis.bambumbot.usecases

import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.entities.train.TrainingSessionRepository
import eu.alkismavridis.bambumbot.usecases.internal.RequireRobot
import org.slf4j.LoggerFactory
import java.util.*

class DeleteRobot {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun delete(idToDelete: UUID, robotRepo: RobotRepository, trainingSessionRepo: TrainingSessionRepository) {
        RequireRobot().require(idToDelete, robotRepo)

        log.debug("Deleting robot with id $idToDelete.")
        StopTraining().stop(idToDelete, robotRepo, trainingSessionRepo)
        robotRepo.delete(idToDelete)
        log.info("Robot with id $idToDelete is deleted.")
    }
}
