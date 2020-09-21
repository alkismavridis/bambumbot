package eu.alkismavridis.bambumbot.entities.data_provider.providers

import eu.alkismavridis.bambumbot.entities.brain.model.NetworkLayer
import eu.alkismavridis.bambumbot.entities.data_provider.DataProvider
import eu.alkismavridis.bambumbot.infrastructure.providers.DataProviderFactory
import eu.alkismavridis.bambumbot.entities.data_provider.NetworkCorrectionUtils

class SubstractionDataProvider(private val range: Double) : DataProvider {
    override fun getInputCount() = 2
    override fun getOutputCount() = 1

    override fun feedInputTo(inputLayer: NetworkLayer): Boolean {
        inputLayer.neurons[0].output = DataProviderUtils.getRandomInRange(this.range)
        inputLayer.neurons[1].output = DataProviderUtils.getRandomInRange(this.range)

        return true
    }

    override fun giveFeedback(inputLayer: NetworkLayer, outputLayer: NetworkLayer) : Double {
        return NetworkCorrectionUtils.applySingleNeuronError(
                inputLayer.neurons[0].output - inputLayer.neurons[1].output,
                outputLayer
        )
    }
}
