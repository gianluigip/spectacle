package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.common.fixtures.aHttpMetadata
import io.gianluigip.spectacle.common.utils.api.getAPIReport
import io.gianluigip.spectacle.common.utils.api.putSpecs
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.api.model.ApiReportResponse
import io.gianluigip.spectacle.report.api.model.ComponentApiResponse
import io.gianluigip.spectacle.report.api.model.ComponentEndpointResponse
import io.gianluigip.spectacle.report.api.model.EndpointRequestResponse
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.api.model.FeatureToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecStatus.IMPLEMENTED
import org.junit.jupiter.api.Test

private const val SPEC_1 = "Spec1"
private const val SPEC_2 = "Spec2"
private const val SPEC_3 = "Spec3"
private const val FEATURE_1 = "Feature1"
private const val FEATURE_2 = "Feature2"
private const val TEAM_1 = "Team1"
private const val TEAM_2 = "Team2"
private const val SOURCE_1 = "Source1"
private const val SOURCE_2 = "Source2"
private const val COMPONENT_1 = "Component1"
private const val COMPONENT_2 = "Component2"
private const val COMPONENT_3 = "Component3"
private const val TAG_1 = "Tag1"
private const val TAG_2 = "Tag2"

@Feature(
    name = Features.API, description = """
   Aggregate all the HTTP Interactions and generate the HTTP API for each service.  
"""
)
@SpecTags(Tags.API, Tags.INTERACTIONS)
class ApiReportIT : BaseIntegrationTest() {

    @Test
    @Specification
    fun `Search the system API by multiple parameters`() =
        given("existing HTTP interactions from multiple sources") {
            val source1 = SpecificationsToUpdateRequest(
                source = SOURCE_1, component = COMPONENT_1, features = listOf(
                    FeatureToUpdateRequest(
                        name = FEATURE_1, description = "",
                        specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_1, name = SPEC_1, status = IMPLEMENTED, tags = listOf(TAG_1), steps = listOf(), interactions = listOf(
                                    SpecInteraction(
                                        OUTBOUND, HTTP, COMPONENT_3,
                                        metadata = aHttpMetadata(
                                            path = "/api/specs", method = "GET", queryParameters = mapOf("p1" to "v1"), requestBody = null,
                                            requestContentType = null, responseStatus = "200", responseBody = """[ { "field1": "val1" } ]""",
                                        ).toMap(),
                                    )
                                )
                            ),
                            SpecificationToUpdateRequest(
                                TEAM_1, name = SPEC_2, status = IMPLEMENTED, tags = listOf(TAG_1), steps = listOf(), interactions = listOf(
                                    SpecInteraction(
                                        OUTBOUND, HTTP, COMPONENT_3, metadata = aHttpMetadata(
                                            path = "/api/specs", method = "POST", requestBody = """{ "specId":"1" }""",
                                            responseStatus = "201", responseBody = """[ { "spec": "details1" } ]""",
                                        ).toMap()
                                    ),
                                )
                            )
                        )
                    ),
                )
            )
            val source2 = SpecificationsToUpdateRequest(
                source = SOURCE_2, component = COMPONENT_2, features = listOf(
                    FeatureToUpdateRequest(
                        name = FEATURE_2, description = "", specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_2,
                                name = SPEC_3,
                                status = SpecStatus.NOT_IMPLEMENTED,
                                tags = listOf(TAG_2),
                                steps = listOf(),
                                interactions = listOf(
                                    SpecInteraction(INBOUND, HTTP, "Int2"),
                                    SpecInteraction(
                                        INBOUND, HTTP, SOURCE_2, metadata = aHttpMetadata(
                                            path = "/api/reports", method = "POST", requestBody = """{ "reportId":"2" }""",
                                            responseStatus = "200", responseBody = """[ { "report": "details2" } ]""",
                                        ).toMap()
                                    )
                                )
                            )
                        )
                    ),
                )
            )
            putSpecs(source1)
            putSpecs(source2)
        } whenever "request a API report for all the endpoints" run {
            getAPIReport()
        } then "the report should contain all the components and endpoints from all the sources" run { report ->
            report shouldBe ApiReportResponse(
                components = listOf(
                    ComponentApiResponse(component = COMPONENT_2, endpoints = listOf(endpointPostReport)),
                    ComponentApiResponse(component = COMPONENT_3, endpoints = listOf(endpointGetSpecs, endpointPostSpec)),
                ),
                filters = filterAll
            )
        } andWhenever "search by parameters" then "the report filters the endpoints accordingly" runAndFinish {
            getAPIReport(source = SOURCE_1) shouldBe ApiReportResponse(
                components = listOf(ComponentApiResponse(component = COMPONENT_3, endpoints = listOf(endpointGetSpecs, endpointPostSpec))),
                filters = filter1
            )
            getAPIReport(feature = FEATURE_2) shouldBe ApiReportResponse(
                components = listOf(ComponentApiResponse(component = COMPONENT_2, endpoints = listOf(endpointPostReport))),
                filters = filter2
            )
            getAPIReport(component = COMPONENT_3) shouldBe ApiReportResponse(
                components = listOf(ComponentApiResponse(component = COMPONENT_3, endpoints = listOf(endpointGetSpecs, endpointPostSpec))),
                filters = filter1
            )
            getAPIReport(tag = TAG_2) shouldBe ApiReportResponse(
                components = listOf(ComponentApiResponse(component = COMPONENT_2, endpoints = listOf(endpointPostReport))),
                filters = filter2
            )
            getAPIReport(team = TEAM_1) shouldBe ApiReportResponse(
                components = listOf(ComponentApiResponse(component = COMPONENT_3, endpoints = listOf(endpointGetSpecs, endpointPostSpec))),
                filters = filter1
            )
            getAPIReport(path = "Report") shouldBe ApiReportResponse(
                components = listOf(ComponentApiResponse(component = COMPONENT_2, endpoints = listOf(endpointPostReport))),
                filters = filter2
            )
        }

    private val endpointGetSpecs = ComponentEndpointResponse(
        features = listOf(FEATURE_1),
        teams = listOf(TEAM_1),
        tags = listOf(TAG_1),
        sources = listOf(SOURCE_1),
        path = "/api/specs",
        method = "GET",
        queryParameters = mapOf("p1" to "v1"),
        requests = listOf(
            EndpointRequestResponse(
                requestBody = null,
                requestContentType = null,
                responseBody = """[ { "field1": "val1" } ]""",
                responseStatus = "200",
                responseContentType = "application/json",
            )
        )
    )

    private val endpointPostSpec = ComponentEndpointResponse(
        features = listOf(FEATURE_1),
        teams = listOf(TEAM_1),
        tags = listOf(TAG_1),
        sources = listOf(SOURCE_1),
        path = "/api/specs",
        method = "POST",
        queryParameters = emptyMap(),
        requests = listOf(
            EndpointRequestResponse(
                requestBody = """{ "specId":"1" }""",
                requestContentType = "application/json",
                responseBody = """[ { "spec": "details1" } ]""",
                responseStatus = "201",
                responseContentType = "application/json",
            )
        )
    )

    private val endpointPostReport = ComponentEndpointResponse(
        features = listOf(FEATURE_2),
        teams = listOf(TEAM_2),
        tags = listOf(TAG_2),
        sources = listOf(SOURCE_2),
        path = "/api/reports",
        method = "POST",
        queryParameters = emptyMap(),
        requests = listOf(
            EndpointRequestResponse(
                requestBody = """{ "reportId":"2" }""",
                requestContentType = "application/json",
                responseBody = """[ { "report": "details2" } ]""",
                responseStatus = "200",
                responseContentType = "application/json",
            )
        )
    )

    private val filterAll = ReportFiltersResponse(
        features = setOf(FEATURE_1, FEATURE_2),
        sources = setOf(SOURCE_1, SOURCE_2),
        components = setOf(COMPONENT_2, COMPONENT_3),
        tags = setOf(TAG_1, TAG_2),
        teams = setOf(TEAM_1, TEAM_2),
        statuses = emptySet(),
    )

    private val filter1 = ReportFiltersResponse(
        features = setOf(FEATURE_1),
        sources = setOf(SOURCE_1),
        components = setOf(COMPONENT_3),
        tags = setOf(TAG_1),
        teams = setOf(TEAM_1),
        statuses = emptySet(),
    )

    private val filter2 = ReportFiltersResponse(
        features = setOf(FEATURE_2),
        sources = setOf(SOURCE_2),
        components = setOf(COMPONENT_2),
        tags = setOf(TAG_2),
        teams = setOf(TEAM_2),
        statuses = emptySet(),
    )
}
