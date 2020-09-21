package eu.alkismavridis.bambumbot.entities.brain.activators

import kotlin.math.tanh

class TanhActivationFunction : ActivationFunction {
    override fun apply(input: Double): Double {
        return tanh(input)
    }

    override fun applyDerivative(input: Double): Double {
        val th = tanh(input)
        return 1.0 - th * th
    }
}
