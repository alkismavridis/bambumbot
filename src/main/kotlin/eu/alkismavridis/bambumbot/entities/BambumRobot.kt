package eu.alkismavridis.bambumbot.entities

import eu.alkismavridis.bambumbot.entities.brain_structure.BrainMetadata
import eu.alkismavridis.bambumbot.entities.brain_structure.BrainConfig
import eu.alkismavridis.bambumbot.entities.data_provider.DataProviderConfig
import eu.alkismavridis.bambumbot.entities.train.TrainerConfig
import java.util.*

class BambumRobot(
        val id: UUID,
        val name: String,
        val brainConfig: BrainConfig,
        val brainMetadata: BrainMetadata,
        val dataProviderConfig: DataProviderConfig,
        val trainerConfig: TrainerConfig,

        val bestState: DoubleArray,
        var leastError: Double,

        val currentState: DoubleArray
) {}
