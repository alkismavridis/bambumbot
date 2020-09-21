package eu.alkismavridis.bambumbot.infrastructure.providers

import eu.alkismavridis.bambumbot.entities.data_provider.DataProvider
import eu.alkismavridis.bambumbot.entities.data_provider.DataProviderConfig
import eu.alkismavridis.bambumbot.entities.data_provider.providers.AdditionDataProvider
import eu.alkismavridis.bambumbot.entities.data_provider.providers.SubstractionDataProvider
import eu.alkismavridis.bambumbot.infrastructure.io.CircularByteStream
import java.lang.IllegalArgumentException

class DataProviderFactory(private val numberStream: CircularByteStream) {
    fun create(config: DataProviderConfig) : DataProvider {
        when(config.type) {
            "ADD" -> return AdditionDataProvider(
                    this.getDouble(config.data?.get("range"), 100.0)
            )

            "SUB" -> return SubstractionDataProvider(
                    this.getDouble(config.data?.get("range"), 100.0)
            )

            "NUMBERS" -> return WrittenNumberDataProvider(numberStream)

            else -> throw IllegalArgumentException("Unknown type ${config.type}")
        }
    }

    private fun getDouble(input: Any?, defaultValue: Double) : Double {
        return if (input is Number) input.toDouble() else defaultValue
    }
}
