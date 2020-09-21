package eu.alkismavridis.bambumbot.usecases

import eu.alkismavridis.bambumbot.entities.BambumRobot
import eu.alkismavridis.bambumbot.entities.RobotRepository
import org.slf4j.LoggerFactory

class GetRobotList {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun get(repo: RobotRepository) : List<BambumRobot> {
        log.debug("Fetching robot list.")
        val result = repo.getAll()
        log.debug("Fetched {} robots.", result.size)

        return result
    }
}
