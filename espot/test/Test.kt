import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.time.Instant

class Test {

    @Test
    fun `script should be fast`() {
        val root = Path.of("test/hierarchy-1/")
        val result = Path.of("result.txt")
        val args = arrayOf("${root.toAbsolutePath()}", "${result.toAbsolutePath()}")

        val startTime = Instant.now()
        Espot_main(args)
        val duration = Duration.between(startTime, Instant.now())

        assertThat(duration).isLessThan(Duration.ofMillis(50))
    }

    @Test
    fun `check result for hierarchy 1`() {
        val root = Path.of("test/hierarchy-1/")
        val result = Path.of("result.txt")
        val args = arrayOf("${root.toAbsolutePath()}", "${result.toAbsolutePath()}")
        val expected = Files.readString(Path.of("test/expected-result-1.txt"))

        Espot_main(args)

        assertThat(Files.readString(result)).isEqualTo(expected)
    }
}
