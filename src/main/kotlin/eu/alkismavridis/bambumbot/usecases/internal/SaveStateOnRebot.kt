package eu.alkismavridis.bambumbot.usecases.internal

import eu.alkismavridis.bambumbot.entities.BambumRobot
import java.lang.IllegalArgumentException

class SaveStateOnRebot {
    fun save(state:DoubleArray, robot:BambumRobot) {
        if (state.size != robot.currentState.size) {
            throw IllegalArgumentException("Cannot load state of unequal size. Expected ${robot.currentState.size}, found ${state.size}.")
        }

        for(i in state.indices) {
            robot.currentState[i] = state[i]
        }
    }
}
