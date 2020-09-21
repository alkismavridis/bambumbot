package eu.alkismavridis.bambumbot.entities.brain_structure

import eu.alkismavridis.bambumbot.entities.brain.model.NetworkLayer

internal class LayersIterator(private val layerStream: Iterator<NetworkLayer>) {
    var current: NetworkLayer? = null; private set
    var next: NetworkLayer? = null; private set

    init {
        this.current = if(this.layerStream.hasNext()) this.layerStream.next() else null
        this.next = if(this.layerStream.hasNext()) this.layerStream.next() else null
    }

    fun progress() {
        this.current = this.next
        this.next = if(this.layerStream.hasNext()) this.layerStream.next() else null
    }
}
