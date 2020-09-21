package eu.alkismavridis.bambumbot.entities.data_provider

import eu.alkismavridis.bambumbot.entities.brain.model.NetworkLayer

interface DataProvider {
    fun feedInputTo(inputLayer: NetworkLayer): Boolean

    /** Returns the square of the total error and sets the errorDerivatives of the output layer. */
    fun giveFeedback(inputLayer: NetworkLayer, outputLayer: NetworkLayer) : Double

    fun getInputCount() : Int
    fun getOutputCount() : Int
}
