package eu.alkismavridis.bambumbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<BambumbotApplication>(*args)
}

@SpringBootApplication
open class BambumbotApplication
