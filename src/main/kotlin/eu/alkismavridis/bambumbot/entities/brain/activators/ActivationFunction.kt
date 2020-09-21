package eu.alkismavridis.bambumbot.entities.brain.activators

interface ActivationFunction {
    fun apply(input: Double) : Double
    fun applyDerivative(input: Double) : Double
}
