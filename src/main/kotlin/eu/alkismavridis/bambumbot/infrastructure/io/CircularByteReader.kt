package eu.alkismavridis.bambumbot.infrastructure.io

import org.slf4j.LoggerFactory
import java.io.BufferedInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path


/** When the end of file has been reached, this object will start from the beginning of the given file. */
class CircularByteStream(private val path:Path) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)!!
    }

    private var inputStream = this.createInputStream()

    fun read() : Int {
        val result = this.inputStream.read()
        if (result >= 0) return result

        log.info("I restart the file")
        this.inputStream = this.createInputStream()
        return this.inputStream.read()
    }


    private fun createInputStream() : InputStream {
        return BufferedInputStream(Files.newInputStream(this.path))
    }
}
