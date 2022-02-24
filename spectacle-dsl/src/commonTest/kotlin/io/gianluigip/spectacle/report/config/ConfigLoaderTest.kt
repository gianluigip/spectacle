package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.report.publisher.TerminalPublisher
import io.gianluigip.spectacle.report.publisher.central.CentralPublisher
import io.ktor.http.Url
import kotlin.test.Ignore
import kotlin.test.Test

class ConfigLoaderTest {

    @Test
    @Ignore //TODO we need to add full support for JavaScript to enable the test again
    fun `should_load_config_from_file`() {
        val config = ConfigLoader.CONFIG
        config assertThat {
            team shouldBe "Spectacle"
            source shouldBe "spectacle-dsl"
            component shouldBe "Spectacle DSL"
            publishers shouldBe listOf(TerminalPublisher, CentralPublisher)
        }
    }
}