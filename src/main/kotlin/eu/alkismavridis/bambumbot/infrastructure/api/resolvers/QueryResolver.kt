package eu.alkismavridis.bambumbot.infrastructure.api.resolvers

import eu.alkismavridis.bambumbot.entities.BambumRobot
import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.usecases.GetRobotList
import eu.alkismavridis.bambumbot.usecases.PredictResult
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import java.util.*


@Component
class QueryResolver(private val robotRepo: RobotRepository) : GraphQLQueryResolver {
    fun robots() : List<BambumRobot> {
        return GetRobotList().get(this.robotRepo)
    }

    fun robot(robotId: UUID) : BambumRobot? {
        return this.robotRepo.get(robotId)
    }

    fun predict(robotId: UUID, input: List<Double>) : List<Double> {
        val result = PredictResult().run(robotId, input.toDoubleArray(), this.robotRepo)
        return result.asList()
    }
}
