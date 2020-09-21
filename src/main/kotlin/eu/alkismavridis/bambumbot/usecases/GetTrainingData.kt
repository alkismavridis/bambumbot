package eu.alkismavridis.bambumbot.usecases

import eu.alkismavridis.bambumbot.entities.train.TrainingSessionRepository
import org.slf4j.LoggerFactory
import java.util.*


class TrainingData(
    val errorBeforeTraining: Double,
    val minimumError: Double,
    val minimumErrorState: DoubleArray
)

class GetTrainingData {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }

    fun get(robotId: UUID, trainingSessionRepo: TrainingSessionRepository): TrainingData? {
        val sessionData = trainingSessionRepo.getSessionFor(robotId)
        if (sessionData?.trainingThread == null) {
            return null
        }

        return TrainingData(
            sessionData.errorBeforeTraining,
            sessionData.minimumError,
            sessionData.minimumErrorState.clone()
        )
    }
}
