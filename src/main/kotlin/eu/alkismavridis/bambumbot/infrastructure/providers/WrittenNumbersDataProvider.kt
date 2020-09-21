package eu.alkismavridis.bambumbot.infrastructure.providers


import eu.alkismavridis.bambumbot.entities.brain.model.NetworkLayer
import eu.alkismavridis.bambumbot.entities.data_provider.DataProvider
import eu.alkismavridis.bambumbot.entities.data_provider.NetworkCorrectionUtils
import eu.alkismavridis.bambumbot.infrastructure.io.CircularByteStream

class WrittenNumberDataProvider(private val numberStream: CircularByteStream) : DataProvider {
    private var lastNumber = -1

    override fun getInputCount() = 28 * 28
    override fun getOutputCount() = 10

    /** we feed 28*28 pixels */
    override fun feedInputTo(inputLayer: NetworkLayer): Boolean {
        this.lastNumber = this.numberStream.read()
        inputLayer.neurons.forEach {
            it.output = this.numberStream.read().toDouble()
        }

        return true
    }

    /**
     * Correct output is defined as:
     * - The [this.lastNumber]-th neuron should indicate 1.0 (which means "yes, this is the number").
     * - Every other neuron should indicate 0.0 (which means no)
     * */
    override fun giveFeedback(inputLayer: NetworkLayer, outputLayer: NetworkLayer): Double {
        val totalError = NetworkCorrectionUtils.applyErrorSquare(outputLayer) { index ->
            if (index == this.lastNumber) 1.0 else 0.0
        }

        return totalError
    }
}
