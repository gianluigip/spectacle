package io.gianluigip.spectacle

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.configureFor
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.report.publisher.SpecificationPublisher
import io.gianluigip.spectacle.report.publisher.TerminalPublisher
import io.gianluigip.spectacle.report.publisher.central.CentralPublisher
import io.ktor.http.Url
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import java.net.ServerSocket

@ExtendWith(JUnitSpecificationReporter::class)
abstract class BaseIntegrationTest {

    private val centralPort = findRandomPort()
    private val wireMockServer = WireMockServer(options().port(centralPort))

    @BeforeEach
    fun initWiremock() {
        wireMockServer.start()
        configureFor("localhost", centralPort);
    }

    @AfterEach
    fun stopWiremock() {
        wireMockServer.stop()
    }

    fun reportConfiguration(
        team: String = "Matching",
        source: String = "spectacle-test",
        component: String = "Spectacle Test",
        publishers: List<SpecificationPublisher> = listOf(TerminalPublisher, CentralPublisher),
        centralEnabled: Boolean = true,
        centralWikiEnabled: Boolean = false,
        localWikiLocation: String? = null,
    ) = ReportConfiguration(
        team,
        source,
        component,
        publishers,
        centralEnabled,
        centralHost = Url("http://localhost:$centralPort"),
        centralWikiEnabled,
        localWikiLocation,
    )

    private fun findRandomPort(): Int {
        ServerSocket(0).use { socket -> return socket.localPort }
    }

}
