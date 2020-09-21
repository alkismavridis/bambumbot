package eu.alkismavridis.bambumbot.infrastructure.config

import eu.alkismavridis.bambumbot.infrastructure.io.CircularByteStream
import eu.alkismavridis.bambumbot.infrastructure.providers.DataProviderFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Paths

@Configuration
open class BambumbotConfig(
        @Value("\${bambumbot.providers.written-numbers-file}")
        private val writtenNumbersFile: String
) {

    @Bean
    open fun dataProviderFactory() : DataProviderFactory {
        val circularPath = CircularByteStream(Paths.get(writtenNumbersFile))
        return DataProviderFactory(circularPath)
    }
}
