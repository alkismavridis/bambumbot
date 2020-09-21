package eu.alkismavridis.bambumbot.usecases.internal

import eu.alkismavridis.bambumbot.entities.BambumRobot
import eu.alkismavridis.bambumbot.entities.RobotRepository
import java.lang.IllegalArgumentException
import java.util.*

class RequireRobot {
    fun require(id: UUID, robotRepo: RobotRepository) : BambumRobot {
        return robotRepo.get(id) ?: throw IllegalArgumentException("Robot with id $id not found")
    }
}
