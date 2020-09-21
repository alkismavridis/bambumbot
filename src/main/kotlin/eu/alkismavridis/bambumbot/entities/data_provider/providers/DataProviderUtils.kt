package eu.alkismavridis.bambumbot.entities.data_provider.providers

class DataProviderUtils {
    companion object {
        fun getRandomInRange(range: Double) : Double {
            return (Math.random() - 0.5) * 2 * range
        }
    }
}
