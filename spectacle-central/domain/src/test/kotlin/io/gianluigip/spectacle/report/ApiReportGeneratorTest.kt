package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.DummyTransactionExecutor
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.common.fixtures.aHttpInteraction
import io.gianluigip.spectacle.common.fixtures.aHttpMetadata
import io.gianluigip.spectacle.common.fixtures.aSpec
import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.report.model.ApiReport
import io.gianluigip.spectacle.report.model.ComponentApi
import io.gianluigip.spectacle.report.model.ComponentEndpoint
import io.gianluigip.spectacle.report.model.EndpointRequest
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private val FEATURE_1 = FeatureName("Feature1")
private val FEATURE_2 = FeatureName("Feature2")
private val FEATURE_3 = FeatureName("Feature3")
private val TEAM_1 = TeamName("Team1")
private val TEAM_2 = TeamName("Team2")
private val SOURCE_1 = Source("Source1")
private val SOURCE_2 = Source("Source2")
private val COMPONENT_1 = Component("Component1")
private val COMPONENT_2 = Component("Component2")
private val TAG_1 = TagName("Tag1")
private val TAG_2 = TagName("Tag2")

@Feature(name = Features.API)
@SpecTags(Tags.API, Tags.INTERACTIONS)
@ExtendWith(JUnitSpecificationReporter::class)
class ApiReportGeneratorTest {

    private val specFinder: SpecificationFinder = mockk()
    private val reportGenerator = ApiReportGenerator(specFinder, DummyTransactionExecutor())

    private lateinit var report: ApiReport

    @Test
    @Specification
    fun `Generate an API Report including all the components with multiple features and sources`() =
        given("existing specs with interactions for multiple features from several sources") {
            every {
                specFinder.findBy(interactionType = HTTP)
            } returns listOf(
                aSpec(
                    SpecName("Spec1"), FEATURE_1, TEAM_1, SOURCE_1, COMPONENT_1, tags = listOf(TAG_1), interactions = listOf(
                        aHttpInteraction(
                            INBOUND, COMPONENT_1, aHttpMetadata(
                                path = "/api/specs", method = "GET", queryParameters = mapOf("p1" to "v1"), requestBody = null,
                                responseStatus = "200", responseBody = """[ { "field1": "val1" } ]""",
                            )
                        ),
                        aHttpInteraction(
                            OUTBOUND, COMPONENT_2, aHttpMetadata(
                                path = "/api/reports", method = "POST", requestBody = """{ "reportId":"1" }""",
                                responseStatus = "201", responseBody = """[ { "report": "details1" } ]""",
                            )
                        )
                    )
                ),
                aSpec(
                    SpecName("Spec2"), FEATURE_1, TEAM_2, SOURCE_2, COMPONENT_2, tags = listOf(TAG_2), interactions = listOf(
                        aHttpInteraction(
                            INBOUND, COMPONENT_2, aHttpMetadata(
                                path = "/api/reports", method = "POST", requestBody = """{ "reportId":"2" }""",
                                responseStatus = "200", responseBody = """[ { "report": "details2" } ]""",
                            )
                        )
                    )
                ),
                aSpec(
                    SpecName("Spec3"), FEATURE_2, TEAM_2, SOURCE_1, COMPONENT_1, tags = listOf(TAG_2), interactions = listOf(
                        aHttpInteraction(
                            INBOUND, COMPONENT_1, aHttpMetadata(
                                path = "/api/specs", method = "GET", queryParameters = mapOf("p3" to "v3"), requestBody = null,
                                responseStatus = "200", responseBody = """[ { "field3": "val3" } ]""",
                            )
                        ),
                    )
                ),
                aSpec(
                    SpecName("Spec4"), FEATURE_2, TEAM_2, SOURCE_1, COMPONENT_1, tags = listOf(TAG_1), interactions = listOf(
                        aHttpInteraction(
                            INBOUND, COMPONENT_1, aHttpMetadata(
                                path = "/api/specs", method = "GET", requestBody = null, responseStatus = "404", responseBody = "",
                            )
                        ),
                    )
                ),
                aSpec(
                    SpecName("Spec5"), FEATURE_3, TEAM_1, SOURCE_2, COMPONENT_2, tags = listOf(TAG_2), interactions = listOf(
                        aHttpInteraction(
                            INBOUND, COMPONENT_2, aHttpMetadata(
                                path = "/api/reports", method = "GET", queryParameters = mapOf("reportId" to "1"),
                                responseStatus = "200", responseBody = """[ { "report": "spec5" } ]""",
                            )
                        )
                    )
                ),
            )
        } whenever "generate a API Report" run {
            report = reportGenerator.generateReport()

        } then "the report aggregate all the endpoints by components and paths" run {
            report.components shouldBe listOf(
                ComponentApi(
                    component = COMPONENT_1,
                    endpoints = listOf(
                        ComponentEndpoint(
                            path = "/api/specs",
                            method = "GET",
                            queryParameters = mapOf("p1" to "v1", "p3" to "v3"),
                            features = listOf(FEATURE_1, FEATURE_2),
                            teams = listOf(TEAM_1, TEAM_2),
                            tags = listOf(TAG_1, TAG_2),
                            sources = listOf(SOURCE_1),
                            requests = listOf(
                                EndpointRequest(
                                    responseStatus = "200",
                                    requestBody = null,
                                    requestContentType = "application/json",
                                    responseBody = """[ { "field1": "val1" } ]""",
                                    responseContentType = "application/json",
                                ),
                                EndpointRequest(
                                    responseStatus = "404",
                                    requestBody = null,
                                    requestContentType = "application/json",
                                    responseBody = "",
                                    responseContentType = "application/json",
                                )
                            )
                        ),
                    )
                ),
                ComponentApi(
                    component = COMPONENT_2,
                    endpoints = listOf(
                        ComponentEndpoint(
                            path = "/api/reports",
                            method = "GET",
                            queryParameters = mapOf("reportId" to "1"),
                            features = listOf(FEATURE_3),
                            teams = listOf(TEAM_1),
                            tags = listOf(TAG_2),
                            sources = listOf(SOURCE_2),
                            requests = listOf(
                                EndpointRequest(
                                    responseStatus = "200",
                                    requestBody = null,
                                    requestContentType = "application/json",
                                    responseBody = """[ { "report": "spec5" } ]""",
                                    responseContentType = "application/json",
                                ),
                            )
                        ),
                        ComponentEndpoint(
                            path = "/api/reports",
                            method = "POST",
                            queryParameters = emptyMap(),
                            features = listOf(FEATURE_1),
                            teams = listOf(TEAM_1, TEAM_2),
                            tags = listOf(TAG_1, TAG_2),
                            sources = listOf(SOURCE_1, SOURCE_2),
                            requests = listOf(
                                EndpointRequest(
                                    responseStatus = "200",
                                    requestBody = """{ "reportId":"2" }""",
                                    requestContentType = "application/json",
                                    responseBody = """[ { "report": "details2" } ]""",
                                    responseContentType = "application/json",
                                ),
                                EndpointRequest(
                                    responseStatus = "201",
                                    requestBody = """{ "reportId":"1" }""",
                                    requestContentType = "application/json",
                                    responseBody = """[ { "report": "details1" } ]""",
                                    responseContentType = "application/json",
                                ),
                            )
                        ),
                    )
                )
            )
        } and "it should include all the filters" runAndFinish {
            report.filters assertThat {
                features shouldBe setOf(FEATURE_1, FEATURE_2, FEATURE_3)
                sources shouldBe setOf(SOURCE_1, SOURCE_2)
                components shouldBe setOf(COMPONENT_1, COMPONENT_2)
                tags shouldBe setOf(TAG_1, TAG_2)
                teams shouldBe setOf(TEAM_1, TEAM_2)
                statuses shouldBe emptySet()
            }
        }
}
