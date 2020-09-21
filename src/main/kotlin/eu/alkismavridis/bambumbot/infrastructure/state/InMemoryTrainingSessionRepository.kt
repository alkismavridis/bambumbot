package eu.alkismavridis.bambumbot.infrastructure.state

import eu.alkismavridis.bambumbot.entities.train.TrainingSession
import eu.alkismavridis.bambumbot.entities.train.TrainingSessionRepository
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryTrainingSessionRepository : TrainingSessionRepository {
    private val runningSessions = ConcurrentHashMap<UUID, TrainingSession>()

    override fun getTrainingSessions(): Collection<TrainingSession> {
        return this.runningSessions.values
    }

    override fun getSessionFor(robotId: UUID): TrainingSession? {
        return this.runningSessions[robotId]
    }

    override fun save(session: TrainingSession) {
        this.runningSessions[session.robotId] = session
    }

    override fun delete(robotId: UUID): TrainingSession? {
        return this.runningSessions.remove(robotId)
    }


    private fun ensureThreadHasStopped(thread: Thread) {
        // TODO
    }
}
