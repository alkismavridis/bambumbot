package eu.alkismavridis.bambumbot.entities.train.trainers

import eu.alkismavridis.bambumbot.entities.brain.model.NetworkLayer
import eu.alkismavridis.bambumbot.entities.brain.model.NeuralNetwork
import eu.alkismavridis.bambumbot.entities.data_provider.DataProvider
import eu.alkismavridis.bambumbot.entities.train.TrainingSession
import java.lang.IllegalStateException
import java.util.stream.Collectors

internal class BatchTrainingState(
    private val network: NeuralNetwork,
    private val dataProvider: DataProvider,
    val learningRate: Double,
    val momentum: Double
) {
    private val inputLayer: NetworkLayer = network.layers[0]
    private val outputLayer: NetworkLayer = network.layers.last()

    // state
    private val trainData1: List<BatchTrainingSynapseData> = network.getSynapseStream().map { BatchTrainingSynapseData(it) }.collect(
        Collectors.toList()
    )
    private val trainData2: List<BatchTrainingSynapseData> = trainData1.map { BatchTrainingSynapseData(it.synapse) }
    private var currentTrainData = this.trainData1
    private var totalBatchError = 0.0
    var batchCount = 0; private set


    fun randomizeSynapses() {
        val range = 50.0 //TODO make this configurable
        this.currentTrainData.forEach { it.synapse.random(range) }
    }

    fun feedInput(): Boolean {
        return this.dataProvider.feedInputTo(this.inputLayer)
    }

    fun makePrediction() {
        this.network.fire()
    }

    fun processFeedback() {
        this.totalBatchError += this.dataProvider.giveFeedback(this.inputLayer, this.outputLayer)
        this.network.backPropagate()

        this.currentTrainData.forEach { it.addToOffset() }
        this.batchCount++
    }

    fun adjustNetwork(session: TrainingSession) {
        val previousTrainData = if (this.currentTrainData == this.trainData1) this.trainData2 else this.trainData1
        val averageError = this.totalBatchError / this.batchCount

        if (averageError < session.minimumError) {
            session.updateMinimum(this.network, averageError)
        }

        for (i in this.currentTrainData.indices) {
            this.updateSynapse(this.currentTrainData[i],  previousTrainData[i])
        }

        this.prepareForNextBatch(previousTrainData)
    }

    private fun updateSynapse(synapseData: BatchTrainingSynapseData, previousSynapseData: BatchTrainingSynapseData) {
        val proposedStep = (-synapseData.offset * this.learningRate + previousSynapseData.offset * this.momentum) / this.batchCount
        val stepToMake = this.getPermitableStep(proposedStep)

        synapseData.synapse.moveBy(stepToMake)
        if (synapseData.synapse.weight.isNaN()) {
            throw IllegalStateException("Synapse weight found to be NaN")
        }
    }

    private fun getPermitableStep(proposedStep: Double) : Double {
        val maxStep = 10.0 //TODO make this configurable
        return when {
            proposedStep > maxStep -> maxStep
            proposedStep < -maxStep -> -maxStep
            else -> proposedStep
        }
    }

    private fun prepareForNextBatch(previousTrainData: List<BatchTrainingSynapseData>) {
        this.totalBatchError = 0.0
        this.currentTrainData = previousTrainData
        this.currentTrainData.forEach { it.resetOffset() }
        this.batchCount = 0
    }

    /// UTILS
    private fun getWeights(network: NeuralNetwork): List<Double> {
        return network.getSynapseStream().map { it.weight }.collect(Collectors.toList())
    }
}
