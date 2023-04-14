package io.gianluigip.spectacle.report

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.exactly
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import io.gianluigip.spectacle.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags.SPECIFICATIONS
import io.gianluigip.spectacle.common.Tags.SYSTEM_DIAGRAM
import io.gianluigip.spectacle.common.fixtures.AuthConstants.CENTRAL_PASSWORD
import io.gianluigip.spectacle.common.fixtures.AuthConstants.CENTRAL_USERNAME
import io.gianluigip.spectacle.common.fixtures.aSpecification
import io.gianluigip.spectacle.common.utils.api.stubPutSpecs
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.publisher.central.CentralPublisher
import io.gianluigip.spectacle.specification.model.HttpInteractionMetadata
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.SpecInteraction
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import io.gianluigip.spectacle.specification.Specification as Spec

@Feature(Features.SYSTEM_DIAGRAM)
@SpecTags(SPECIFICATIONS, SYSTEM_DIAGRAM)
class SpectacleInteractionsIT : BaseIntegrationTest() {

    private lateinit var specs: List<Spec>

    @Test
    @Specification
    fun `The HTTP interactions should be published on Central`() =
        given("a unit test that registered multiple HTTP interactions") {

            val spec1 = aSpecification(
                interactions = listOf(
                    SpecInteraction(
                        direction = INBOUND, type = HTTP, name = "Int1", metadata = HttpInteractionMetadata(
                            path = "api/specs", method = "GET", queryParameters = mapOf("p1" to "v1", "p2" to "v2"),
                            requestBody = """{ "input": "1" }""", requestContentType = "application/json",
                            responseBody = """{ "output": "2" }""", responseStatus = "200", responseContentType = "application/json",
                        ).toMap()
                    ),
                    SpecInteraction(
                        direction = INBOUND, type = HTTP, name = "Int2", metadata = HttpInteractionMetadata(
                            path = "api/report/finance", method = "POST", queryParameters = emptyMap(),
                            requestBody = """{ "input": "3" }""", requestContentType = "application/json",
                            responseBody = """{ "output": "4" }""", responseStatus = "201", responseContentType = "application/json",
                        ).toMap()
                    ),
                    SpecInteraction(
                        direction = INBOUND, type = HTTP, name = "Int3", metadata = HttpInteractionMetadata(
                            path = "api/specs/1234", method = "DELETE", queryParameters = emptyMap(),
                            requestBody = null, requestContentType = null,
                            responseBody = "", responseStatus = "404", responseContentType = null,
                        ).toMap()
                    )
                )
            )
            specs = listOf(spec1)

        } whenever "the tests finished to execute" run {
            stubPutSpecs()
            val config = reportConfiguration()
            runBlocking { CentralPublisher.publishReport(specs, config) }

        } then "it should publish the HTTP interactions into Spectacle Central" runAndFinish {
            verify(
                exactly(1),
                putRequestedFor(urlEqualTo("/api/specification"))
                    .withBasicAuth(BasicCredentials(CENTRAL_USERNAME, CENTRAL_PASSWORD))
                    .withRequestBody(
                        equalToJson(
                            """
                        {
                          "source": "spectacle-test",
                          "component": "Spectacle Test",
                          "features": [
                            {
                              "name": "Feature1",
                              "description": "Description1",
                              "specs": [
                                {
                                  "team": "Team1",
                                  "name": "Spec1",
                                  "status": "IMPLEMENTED",
                                  "tags": ["Tag1"],
                                  "steps": [
                                    {
                                      "type": "WHENEVER",
                                      "description": "Step1",
                                      "index": 0
                                    }
                                  ],
                                  "interactions": [
                                    {
                                      "direction": "INBOUND",
                                      "type": "HTTP",
                                      "name": "Int1",
                                      "metadata": {
                                        "metadataType": "HTTP",
                                        "path": "api/specs",
                                        "method": "GET",
                                        "queryParameters": "{  \"p1\": \"v1\" ,  \"p2\": \"v2\"  }",
                                        "requestContentType": "application/json",
                                        "requestBody": "{ \"input\": \"1\" }",
                                        "responseStatus": "200",
                                        "responseContentType": "application/json",
                                        "responseBody": "{ \"output\": \"2\" }"
                                      }
                                    },
                                    {
                                      "direction": "INBOUND",
                                      "type": "HTTP",
                                      "name": "Int2",
                                      "metadata": {
                                        "metadataType" : "HTTP",
                                        "path": "api/report/finance",
                                        "method": "POST",
                                        "queryParameters": "{  }",
                                        "requestContentType": "application/json",
                                        "requestBody": "{ \"input\": \"3\" }",
                                        "responseStatus": "201",
                                        "responseContentType": "application/json",
                                        "responseBody": "{ \"output\": \"4\" }"
                                      }
                                    },
                                    {
                                      "direction": "INBOUND",
                                      "type": "HTTP",
                                      "name": "Int3",
                                      "metadata": {
                                        "metadataType" : "HTTP",
                                        "path": "api/specs/1234",
                                        "method": "DELETE",
                                        "queryParameters": "{  }",
                                        "responseBody": "",
                                        "responseStatus": "404"
                                      }
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        }"""
                        )
                    )
            )
        }
}
