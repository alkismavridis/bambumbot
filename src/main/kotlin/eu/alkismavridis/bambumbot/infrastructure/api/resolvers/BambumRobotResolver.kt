package eu.alkismavridis.bambumbot.infrastructure.api.resolvers

import eu.alkismavridis.bambumbot.entities.BambumRobot
import eu.alkismavridis.bambumbot.entities.train.TrainingSessionRepository
import eu.alkismavridis.bambumbot.usecases.GetTrainingData
import eu.alkismavridis.bambumbot.usecases.TrainingData
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class BambumRobotResolver(private val trainingSessionRepo: TrainingSessionRepository) : GraphQLResolver<BambumRobot> {
    fun bestState(robot:BambumRobot) : List<Double> {
        return robot.bestState.asList()
    }

    fun currentState(robot:BambumRobot) : List<Double> {
        return robot.currentState.asList()
    }

    fun trainingData(robot:BambumRobot) : TrainingData? {
        return GetTrainingData().get(robot.id, this.trainingSessionRepo)
    }
}
