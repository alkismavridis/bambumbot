package eu.alkismavridis.bambumbot.usecases

import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.entities.brain_state.StateLoader
import eu.alkismavridis.bambumbot.entities.brain_structure.StructureUtils
import eu.alkismavridis.bambumbot.usecases.internal.RequireRobot
import java.lang.IllegalArgumentException
import java.util.*

class PredictResult {
    fun run(robotId: UUID, input:DoubleArray, robotRepo:RobotRepository) : DoubleArray {
        val robot = RequireRobot().require(robotId, robotRepo)
        val brain = StructureUtils.generate(robot.brainConfig)
        StateLoader.load(brain, robot.currentState)

        val expectedInputSize = brain.layers[0].neurons.size
        if (expectedInputSize != input.size) {
            throw IllegalArgumentException("Invalid input size. Expected $expectedInputSize but got ${input.size}")
        }

        for (i in input.indices) {
            brain.layers[0].neurons[i].output = input[i]
        }
        brain.fire()

        return brain.layers.last().neurons.map{ it.output }.toDoubleArray()
    }
}
