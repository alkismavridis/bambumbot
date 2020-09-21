package eu.alkismavridis.bambumbot.entities.data_provider

import eu.alkismavridis.bambumbot.entities.brain.model.NetworkLayer

class NetworkCorrectionUtils {
    companion object {
        fun applySingleNeuronError(expectedValue: Double, outputLayer: NetworkLayer) : Double {
            val outNeuron = outputLayer.neurons[0]

            outNeuron.errorDerivative = 2 * (outNeuron.output - expectedValue)
            return outNeuron.errorDerivative * outNeuron.errorDerivative / 4
        }

        fun applyErrorSquare(outputLayer: NetworkLayer, expectedValueProvider: (i:Int) -> Double) : Double {
            var sumError = 0.0
            for (i in 0 until outputLayer.neurons.size) {
                val outNeuron = outputLayer.neurons[i]
                outNeuron.errorDerivative = 2 * (outNeuron.output - expectedValueProvider(i))
                sumError += outNeuron.errorDerivative * outNeuron.errorDerivative / 4

            }

            return sumError
        }
    }
}
