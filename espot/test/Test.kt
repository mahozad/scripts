@file:Suppress("unused", "HasPlatformType")

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.time.Instant

var i = 1

fun argProvider() = File("test")
    .listFiles()!!
    .sorted()
    .filter { it.name.startsWith("seed") }
    .map(File::toPath)
    .map(Path::toAbsolutePath)
    .map { Arguments.of(it, File("test/expected-result-${i++}.txt").readText()) }

class Test {

    @Test
    fun `script should be fast`() {
        val seed = Path.of("test/seed-1/")
        val result = Path.of("result.txt")
        val args = arrayOf("${seed.toAbsolutePath()}", "$result")
        val startTime = Instant.now()
        Espot_main(args)
        val duration = Duration.between(startTime, Instant.now())
        assertThat(duration).isLessThan(Duration.ofMillis(50))
    }

    @Test
    fun `check result for empty seed`() {
        val seed = Path.of("test/seed-4/")
        // Ensure the empty directory exists because couldn't commit it to Git
        Files.deleteIfExists(seed)
        Files.createDirectory(seed)
        val result = Path.of("result.txt")
        val args = arrayOf("$seed", "$result")
        val expected = Files.readString(Path.of("test/expected-result-4.txt"))
        Espot_main(args)
        assertThat(Files.readString(result)).isEqualTo(expected)
    }

    @ParameterizedTest(name = "check result for seed {index}")
    @MethodSource("TestKt#argProvider")
    fun `check result for seeds`(seed: Path, expected: String) {
        val result = Path.of("result.txt")
        val args = arrayOf("$seed", "$result")
        Espot_main(args)
        assertThat(Files.readString(result)).isEqualTo(expected)
    }
}
