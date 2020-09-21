package eu.alkismavridis.bambumbot.entities.brain.activators

private const val REALU_LEAK_FACTOR = 0.001

class LeakyReluActivationFunction : ActivationFunction {
    override fun apply(input: Double): Double {
        return if (input > 0) input else REALU_LEAK_FACTOR * input
    }

    override fun applyDerivative(input: Double): Double {
        return if (input > 0) 1.0 else REALU_LEAK_FACTOR
    }
}
