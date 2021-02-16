import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ScriptTest {

    @Test
    fun firstTest() {
        val os = Script_main(emptyArray()).getEnvironmentVariable("OS")
        assertEquals("Windows_NT", os)
    }
}
