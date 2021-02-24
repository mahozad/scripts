import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Test {

    @Test
    fun pass() {
        assertThat(Espot_main(emptyArray()).root).isNotNull()
    }
}
