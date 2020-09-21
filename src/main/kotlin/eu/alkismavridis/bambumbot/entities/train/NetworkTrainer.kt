package eu.alkismavridis.bambumbot.entities.train

import eu.alkismavridis.bambumbot.entities.brain.model.NeuralNetwork
import eu.alkismavridis.bambumbot.entities.data_provider.DataProvider

interface NetworkTrainer {
    fun start(network: NeuralNetwork, dataProvider: DataProvider, session: TrainingSession)
}
