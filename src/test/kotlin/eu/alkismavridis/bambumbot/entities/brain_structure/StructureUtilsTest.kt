package eu.alkismavridis.bambumbot.entities.brain_structure

import eu.alkismavridis.bambumbot.entities.brain.activators.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class StructureUtilsTest {
    @Test
    fun shouldCreateLinear() {
        assertThat(StructureUtils.from("LINEAR"))
                .isExactlyInstanceOf(LinearActivationFunction::class.java)
    }

    @Test
    fun shouldCreateRelu() {
        assertThat(StructureUtils.from("RELU"))
                .isExactlyInstanceOf(ReluActivationFunction::class.java)
    }

    @Test
    fun shouldCreateLeakyRelu() {
        assertThat(StructureUtils.from("LEAKY_RELU"))
                .isExactlyInstanceOf(LeakyReluActivationFunction::class.java)
    }

    @Test
    fun shouldCreateSigmoid() {
        assertThat(StructureUtils.from("SIGMOID"))
                .isExactlyInstanceOf(SigmoidActivationFunction::class.java)
    }

    @Test
    fun shouldCreateTanh() {
        assertThat(StructureUtils.from("TANH"))
                .isExactlyInstanceOf(TanhActivationFunction::class.java)
    }

    @Test
    fun shouldThrowOnUnknownCode() {
        assertThatThrownBy{StructureUtils.from("I_DO_NOT_EXIST")}
                .isExactlyInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Unknown activation function code: \"I_DO_NOT_EXIST\"")
    }
}
