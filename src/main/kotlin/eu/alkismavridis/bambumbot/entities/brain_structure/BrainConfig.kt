package eu.alkismavridis.bambumbot.entities.brain_structure

class LayerConfig {
    var type: String = ""
    var neuronCount = 0
    var times = 1
    var activation = ""
}

open class BrainConfig {
    var layers = mutableListOf<LayerConfig>()
    var activation: String = ""
}
