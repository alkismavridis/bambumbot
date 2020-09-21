package eu.alkismavridis.bambumbot.usecases

import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.usecases.internal.RequireRobot
import eu.alkismavridis.bambumbot.usecases.internal.SaveStateOnRebot
import org.slf4j.LoggerFactory
import java.util.*

class SetRobotState {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun set(robotId: UUID, state:DoubleArray, robotRepository: RobotRepository) {
        log.debug("Loading state for robot $robotId...")

        val robot = RequireRobot().require(robotId, robotRepository)
        SaveStateOnRebot().save(state, robot)
        robotRepository.save(robot)

        log.info("Loaded state for robot $robotId.")
    }
}
