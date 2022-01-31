package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.report.publisher.TerminalPublisher
import kotlin.test.Test

class ConfigLoaderTest {

    @Test
    fun `should load config from file`() {
        val config = ConfigLoader.CONFIG
        config assertThat {
            team shouldBe "Spectacle"
            source shouldBe "spectacle-dsl"
            component shouldBe "Spectacle DSL"
            publishers shouldBe listOf(TerminalPublisher)
        }
    }
}