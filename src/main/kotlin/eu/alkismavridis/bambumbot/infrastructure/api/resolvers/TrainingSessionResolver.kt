package eu.alkismavridis.bambumbot.infrastructure.api.resolvers

import eu.alkismavridis.bambumbot.entities.train.TrainingSession
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class TrainingSessionResolver : GraphQLResolver<TrainingSession> {
    fun minimumErrorState(session:TrainingSession) : List<Double> {
        return session.minimumErrorState.asList()
    }
}
