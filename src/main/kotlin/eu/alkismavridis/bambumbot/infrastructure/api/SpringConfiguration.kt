package eu.alkismavridis.bambumbot.infrastructure.api

import eu.alkismavridis.bambumbot.entities.brain_structure.LayerConfig
import eu.alkismavridis.bambumbot.entities.brain_structure.BrainConfig
import eu.alkismavridis.bambumbot.entities.data_provider.DataProviderConfig
import eu.alkismavridis.bambumbot.entities.train.TrainerConfig
import graphql.kickstart.tools.SchemaParserDictionary
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class SpringConfiguration {
    @Bean
    open fun graphqlDictionary() : SchemaParserDictionary {
        return SchemaParserDictionary()
            .add("DataProviderConfigInput", DataProviderConfig::class.java)
            .add("TrainerConfigInput", TrainerConfig::class.java)
            .add("NetworkConfigInput", BrainConfig::class.java)
            .add("LayerConfigInput", LayerConfig::class.java)
    }


    @Bean
    open fun graphqlAnyScalar() : GraphQLScalarType {
        return ExtendedScalars.Json
    }
}
