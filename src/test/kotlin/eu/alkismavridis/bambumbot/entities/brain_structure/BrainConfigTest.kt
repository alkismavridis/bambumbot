package eu.alkismavridis.bambumbot.entities.brain_structure

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eu.alkismavridis.bambumbot.entities.brain.activators.LinearActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.activators.ReluActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.activators.SigmoidActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.activators.TanhActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.activators.ActivationFunction
import eu.alkismavridis.bambumbot.entities.brain.model.NetworkLayer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

class BrainConfigTest {
    val mapper = jacksonObjectMapper()

    @Test
    fun shouldGenerateEmptyNetwork() {
        val str = this.load("NetworkStructures/empty-structure.json")
        val result = StructureUtils.generate(str)

        assertThat(result.getNeuronCount()).isEqualTo(0)
        assertThat(result.getSynapseCount()).isEqualTo(0)
        assertThat(result.layers.size).isEqualTo(0)
    }

    @Test
    fun shouldGenerateSingleLayerNetwork() {
        val str = this.load("NetworkStructures/minimal-layer.json")
        val result = StructureUtils.generate(str)

        assertThat(result.getNeuronCount()).isEqualTo(2)
        assertThat(result.getSynapseCount()).isEqualTo(0)
        assertThat(result.layers.size).isEqualTo(1)
    }

    @Test
    fun shouldGenerateFullyConnectedLayerNetwork() {
        val str = this.load("NetworkStructures/fully-connected.json")
        val result = StructureUtils.generate(str)

        assertThat(result.getNeuronCount()).isEqualTo(7)
        assertThat(result.getSynapseCount()).isEqualTo(17)
        assertThat(result.layers.size).isEqualTo(3)

        this.assertFullyConnectedLayer(result.layers[0], 2, 9, SigmoidActivationFunction::class.java, result.layers[1])
        this.assertFullyConnectedLayer(result.layers[1], 3, 8, SigmoidActivationFunction::class.java, result.layers[2])
        this.assertOutputLayer(result.layers[2], 2, LinearActivationFunction::class.java)
    }

    @Test
    fun shouldGenerateFullyConnectedRepeatingLayer() {
        val str = this.load("NetworkStructures/repeated-layer.json")
        val result = StructureUtils.generate(str)

        assertThat(result.getNeuronCount()).isEqualTo(13)
        assertThat(result.getSynapseCount()).isEqualTo(41)
        assertThat(result.layers.size).isEqualTo(5)

        this.assertFullyConnectedLayer(result.layers[0], 2, 9, SigmoidActivationFunction::class.java, result.layers[1])
        this.assertFullyConnectedLayer(result.layers[1], 3, 12, SigmoidActivationFunction::class.java, result.layers[2])
        this.assertFullyConnectedLayer(result.layers[2], 3, 12, SigmoidActivationFunction::class.java, result.layers[3])
        this.assertFullyConnectedLayer(result.layers[3], 3, 8, SigmoidActivationFunction::class.java, result.layers[4])
        this.assertOutputLayer(result.layers[4], 2, LinearActivationFunction::class.java)
    }

    @Test
    fun shouldHandleLayerLevelActivationFunctions() {
        val str = this.load("NetworkStructures/various-activation-functions.json")
        val result = StructureUtils.generate(str)

        assertThat(result.getNeuronCount()).isEqualTo(12)
        assertThat(result.getSynapseCount()).isEqualTo(31)
        assertThat(result.layers.size).isEqualTo(6)

        this.assertFullyConnectedLayer(result.layers[0], 2, 9, SigmoidActivationFunction::class.java, result.layers[1])
        this.assertFullyConnectedLayer(result.layers[1], 3, 12, TanhActivationFunction::class.java, result.layers[2])
        this.assertFullyConnectedLayer(result.layers[2], 3, 4, TanhActivationFunction::class.java, result.layers[3])
        this.assertFullyConnectedLayer(result.layers[3], 1, 2, ReluActivationFunction::class.java, result.layers[4])
        this.assertFullyConnectedLayer(result.layers[4], 1, 4, ReluActivationFunction::class.java, result.layers[5])
        this.assertOutputLayer(result.layers[5], 2, SigmoidActivationFunction::class.java)
    }


    @Test
    fun shouldThrowIfOutIsNotTheLastLayer() {
        val str = this.load("NetworkStructures/error/illegal-output-layer.json")
        assertThatThrownBy{ StructureUtils.generate(str) }
                .isExactlyInstanceOf(IllegalStateException::class.java)
                .hasMessage("Only the last layer can be an output layer")
    }

    @Test
    fun shouldThrowIfOutLayerIsRepeated() {
        val str = this.load("NetworkStructures/error/illegal-repeated-output-layer.json")
        assertThatThrownBy{ StructureUtils.generate(str) }
                .isExactlyInstanceOf(IllegalStateException::class.java)
                .hasMessage("Only the last layer can be an output layer")
    }

    @Test
    fun shouldThrowIfLastLayerIsNotOutput() {
        val str = this.load("NetworkStructures/error/illegal-end-layer.json")
        assertThatThrownBy{ StructureUtils.generate(str) }
                .isExactlyInstanceOf(IllegalStateException::class.java)
                .hasMessage("Last layer must have type: OUT")
    }

    @Test
    fun shouldThrowIfLayerHasUnknownType() {
        val str = this.load("NetworkStructures/error/illegal-layer-type.json")
        assertThatThrownBy{ StructureUtils.generate(str) }
                .isExactlyInstanceOf(IllegalStateException::class.java)
                .hasMessage("Unknown layer type: WRONG_TYPE")
    }


    /// UTILS
    private fun load(pathString:String) : BrainConfig {
        return this.mapper.readValue(this.javaClass.classLoader.getResourceAsStream(pathString), BrainConfig::class.java)
    }

    private fun assertFullyConnectedLayer(fromLayer:NetworkLayer, expectedNeuronCount:Int, expectedSynapseCount:Int, activationClass:Class<out ActivationFunction>, toLayer: NetworkLayer) {
        assertThat(fromLayer.neurons.size).isEqualTo(expectedNeuronCount)
        assertThat(fromLayer.getSynapseCount()).isEqualTo(expectedSynapseCount)
        for (i in 0 until fromLayer.neurons.size) {
            assertThat(fromLayer.neurons[i].activationFunc).isExactlyInstanceOf(activationClass)
            for (j in 0 until toLayer.neurons.size) {
                assertThat(fromLayer.neurons[i].synapses).anyMatch{it.target === toLayer.neurons[j]}
            }
        }

        //check bias
        for (j in 0 until toLayer.neurons.size) {
            assertThat(fromLayer.bias.synapses).anyMatch{it.target === toLayer.neurons[j]}
        }
    }

    private fun assertOutputLayer(outLayer:NetworkLayer, expectedNeuronCount:Int, activationClass:Class<out ActivationFunction>) {
        assertThat(outLayer.neurons.size).isEqualTo(expectedNeuronCount)
        assertThat(outLayer.getSynapseCount()).isEqualTo(0)
        for (i in 0 until outLayer.neurons.size) {
            assertThat(outLayer.neurons[i].activationFunc).isExactlyInstanceOf(activationClass)
            assertThat(outLayer.neurons[i].synapses).isEmpty()
        }

        assertThat(outLayer.bias.synapses).isEmpty()
    }
}
