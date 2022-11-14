package io.gianluigip.spectacle.dsl.interactions

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.put
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.dsl.bdd.aSpec
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.coRun
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.ServerSocket

@Feature(Features.SYSTEM_DIAGRAM)
@SpecTags(Tags.SYSTEM_DIAGRAM, Tags.INTERACTIONS)
@ExtendWith(JUnitSpecificationReporter::class)
class HttpClientInteractionsDslIT {

    private val port = findRandomPort()
    private val wireMockServer = WireMockServer(WireMockConfiguration.options().port(port))

    @BeforeEach
    fun initWiremock() {
        wireMockServer.start()
        WireMock.configureFor("localhost", port)
        httpInteractionsConfig("http://localhost:$port", httpClient = HttpClient {
            install(ContentNegotiation) { json() }
        })
    }

    @AfterEach
    fun stopWiremock() {
        wireMockServer.stop()
    }

    @Test
    @Specification
    fun `The HTTP Interactions DSL can execute HTTP requests and generate documentation from it`() {
        val spec = given("an integration test") {
        } whenever "it has to verify a HTTP request" run {
            stubFor(
                put("/api/specs/1234?p1=v1&p2=v2")
                    .withHeader(HttpHeaders.ContentType, equalTo(ContentType.Application.Json.toString()))
                    .withRequestBody(equalToJson(""" { "field1": "val1", "field2": "val2" } """))
                    .willReturn(ok(""" { "field3": "val3", "field4": "val4" } """))
            )
        } then "it can use the HTTP Interactions to make the request and document the endpoint" coRun {
            val testSpec = aSpec() whenever "run" coRun {
                receivesPutRequest(
                    path = "/api/specs/{SPEC_ID}",
                    pathParameters = mapOf("SPEC_ID" to "1234"),
                    queryParameters = mapOf("p1" to "v1", "p2" to "v2"),
                    contentType = ContentType.Application.Json,
                    body = """
                        {
                            "field1": "val1",
                            "field2": "val2"
                        }
                    """.trimIndent(),
                    fromComponent = "TestService",
                )
            }

            testSpec.specBuilder.build().interactions.first() shouldBe SpecInteraction(
                direction = InteractionDirection.INBOUND,
                type = InteractionType.HTTP,
                name = "TestService",
                metadata = mapOf(
                    "path" to "/api/specs/{SPEC_ID}",
                    "method" to "PUT",
                    "queryParameters" to """{  "p1": "v1" ,  "p2": "v2"  }""",
                    "requestBody" to """
                        {
                            "field1": "val1",
                            "field2": "val2"
                        }""".trimIndent(),
                    "requestContentType" to "application/json",
                    "responseBody" to """{ "field3": "val3", "field4": "val4" }""",
                    "responseStatus" to "200"
                )
            )
        }
        TestContext.setCurrentSpec(spec.specBuilder)
    }


    private fun findRandomPort(): Int {
        ServerSocket(0).use { socket -> return socket.localPort }
    }
}
