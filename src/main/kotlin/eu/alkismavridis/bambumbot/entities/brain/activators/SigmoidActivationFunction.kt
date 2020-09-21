package eu.alkismavridis.bambumbot.entities.brain.activators

import kotlin.math.exp

class SigmoidActivationFunction : ActivationFunction {
    override fun apply(input: Double): Double {
        return 1/(1+ exp(-input))
    }

    override fun applyDerivative(input: Double): Double {
        val act = 1 / (1 + exp(-input))
        return act * (1 - act)
    }
}
