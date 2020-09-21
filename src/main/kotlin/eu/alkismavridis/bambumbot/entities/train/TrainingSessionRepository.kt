package eu.alkismavridis.bambumbot.entities.train

import java.util.*

interface TrainingSessionRepository {
    fun getTrainingSessions() : Collection<TrainingSession>
    fun getSessionFor(robotId: UUID): TrainingSession?
    fun save(session: TrainingSession)
    fun delete(robotId: UUID) : TrainingSession?
}
