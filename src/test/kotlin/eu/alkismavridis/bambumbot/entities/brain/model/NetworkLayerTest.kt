package eu.alkismavridis.bambumbot.entities.brain.model

import eu.alkismavridis.bambumbot.entities.brain.activators.LinearActivationFunction
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*


internal class NetworkLayerTest {
    @Test
    fun reset_shouldSetAllInputsToZero() {
        val layer = NetworkLayer()
        layer.addNeuron(Neuron(LinearActivationFunction()))
        layer.addNeuron(Neuron(LinearActivationFunction()))

        layer.neurons[0].input = 4.0
        layer.neurons[1].input = 6.0
        layer.bias.input = 8.5

        layer.reset()
        assertThat(layer.neurons[0].input).isEqualTo(0.0)
        assertThat(layer.neurons[1].input).isEqualTo(0.0)
        assertThat(layer.bias.input).isEqualTo(8.5) //bias should not be reset
    }

    @Test
    fun fire_shouldCauseAllNeuronsToFire() {
        val layer = NetworkLayer(mock(Neuron::class.java))
        layer.addNeuron(mock(Neuron::class.java))
        layer.addNeuron(mock(Neuron::class.java))

        layer.fire()
        verify(layer.neurons[0], times(1)).fire()
        verify(layer.neurons[1], times(1)).fire()
        verify(layer.bias, times(1)).fire()
    }

    @Test
    fun activate_shouldCauseAllNeuronsToActivate() {
        val layer = NetworkLayer(mock(Neuron::class.java))
        layer.addNeuron(mock(Neuron::class.java))
        layer.addNeuron(mock(Neuron::class.java))

        layer.activate()
        verify(layer.neurons[0], times(1)).activate()
        verify(layer.neurons[1], times(1)).activate()
        verify(layer.bias, never()).activate()
    }
}
