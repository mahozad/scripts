import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScriptTest {

    @Test
    fun `OS should be Windows`() {
        val os = Script_main(emptyArray()).getEnvironmentVariable("OS")
        assertThat(os).isEqualTo("Windows_NT") // Using AssertJ assertion
        assertEquals("Windows_NT", os) // Using JUnit assertion
    }
}
