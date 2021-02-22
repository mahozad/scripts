import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScriptTest {

    @Test
    fun `OS Should be Windows`() {
        val os = Script_main(emptyArray()).os
        assertEquals("Windows_NT", os) // Using JUnit assertion
    }

    @Test
    fun `Program Arguments Should be Converted to All Caps`() {
        val args = arrayOf("Hello", "world")
        val expected = listOf("HELLO", "WORLD")
        val result = Script_main(args).capitalizeArgs()
        assertThat(result).isEqualTo(expected) // Using AssertJ assertion
    }
}
