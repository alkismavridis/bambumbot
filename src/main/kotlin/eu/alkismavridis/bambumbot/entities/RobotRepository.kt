package eu.alkismavridis.bambumbot.entities

import java.util.*

interface RobotRepository {
    fun getAll() : List<BambumRobot>
    fun get(id: UUID) : BambumRobot?
    fun save(robot: BambumRobot)
    fun delete(id: UUID)
}
