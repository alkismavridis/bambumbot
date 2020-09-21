package eu.alkismavridis.bambumbot.entities.brain.activators

class LinearActivationFunction : ActivationFunction {
    override fun apply(input: Double): Double {
        return input
    }

    override fun applyDerivative(input: Double): Double {
        return 1.0
    }
}
