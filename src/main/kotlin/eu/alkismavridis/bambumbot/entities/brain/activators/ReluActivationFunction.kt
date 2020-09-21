package eu.alkismavridis.bambumbot.entities.brain.activators

class ReluActivationFunction : ActivationFunction {
    override fun apply(input: Double): Double {
        return if (input > 0) input else 0.0
    }

    override fun applyDerivative(input: Double): Double {
        return if (input > 0) 1.0 else 0.0
    }
}
