package eu.alkismavridis.bambumbot.entities.train

import eu.alkismavridis.bambumbot.entities.train.trainers.BatchTrainer
import java.lang.IllegalArgumentException

class TrainerUtils {
    companion object {
        fun generate(config: TrainerConfig) : NetworkTrainer {
            when(config.type) {
                "BATCH" -> return BatchTrainer(
                    getInt(config.data?.get("batchSize"), 25),
                    getDouble(config.data?.get("learningRate"), 0.0001),
                    getDouble(config.data?.get("momentum"), 0.0)
                )
                else -> throw IllegalArgumentException("Unknown type ${config.type}")
            }
        }

        private fun getDouble(input: Any?, defaultValue: Double) : Double {
            return if (input is Number) input.toDouble() else defaultValue
        }

        private fun getInt(input: Any?, defaultValue: Int) : Int {
            return if (input is Number) input.toInt() else defaultValue
        }
    }
}
