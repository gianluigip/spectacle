package io.gianluigip.spectacle.report

import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.exactly
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import io.gianluigip.spectacle.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags.SPECIFICATIONS
import io.gianluigip.spectacle.common.utils.api.stubPutSpecs
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.publisher.central.CentralPublisher
import io.gianluigip.spectacle.specification.SpecificationMetadata
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.StepType.WHENEVER
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import io.gianluigip.spectacle.specification.Specification as Spec
import io.gianluigip.spectacle.specification.model.SpecificationStep as Step

@Feature(
    Features.SPECIFICATIONS,
    description = "Spectacle can publish the specs into a central repository so it can be review it by any stakeholder."
)
@SpecTags(SPECIFICATIONS)
class SpectacleReportIT : BaseIntegrationTest() {

    private lateinit var specs: List<Spec>

    @Test
    @Specification
    fun `The specifications should be published on Central`() =
        given("unit tests defining Spec1 and Spec2 for Feature1") {
            val spec1Metadata = SpecificationMetadata(
                featureName = "Feature1", featureDescription = "\nDescription1\n\t\tMulti line\n", team = "Matching",
                status = SpecStatus.IMPLEMENTED, tags = listOf("Tag1-1", "Tag1-2")
            )
            val spec2Metadata = SpecificationMetadata(
                featureName = "Feature1", featureDescription = "", team = "Matching", status = SpecStatus.NOT_IMPLEMENTED,
                tags = listOf("Tag2")
            )
            val spec1 = Spec(
                metadata = spec1Metadata, name = "Spec1", steps = mutableListOf(Step(WHENEVER, "Step1", 0)),
                interactions = listOf(SpecInteraction(INBOUND, EVENT, "Int1", mutableMapOf("m1" to "v1")))
            )
            val spec2 = Spec(
                metadata = spec2Metadata, name = "Spec2", steps = mutableListOf(Step(GIVEN, "Step2", 0)),
                interactions = listOf(SpecInteraction(OUTBOUND, HTTP, "Int2", emptyMap()))
            )
            specs = listOf(spec1, spec2)

        } whenever "the tests finished to execute" run {
            stubPutSpecs()
            val config = reportConfiguration()
            runBlocking { CentralPublisher.publishReport(specs, config) }

        } then "it should publish Spec1 and Spec2 in Spectacle Central" runAndFinish {
            verify(
                exactly(1),
                putRequestedFor(urlEqualTo("/api/specification"))
                    .withRequestBody(
                        equalToJson(
                            """
                        {
                            "source": "spectacle-test",
                            "component": "Spectacle Test",
                            "features": [
                                {
                                    "name": "Feature1",
                                    "description": "Description1\nMulti line",
                                    "specs": [
                                        {
                                            "team": "Matching",
                                            "name": "Spec1",
                                            "status": "IMPLEMENTED",
                                            "tags": [ "Tag1-1", "Tag1-2" ],
                                            "steps": [ { "type": "WHENEVER", "description": "Step1", "index": 0 } ],
                                            "interactions" : [
                                                {
                                                    "direction": "INBOUND",
                                                    "type": "EVENT",
                                                    "name": "Int1",
                                                    "metadata": {
                                                        "m1": "v1"
                                                    }
                                                }
                                            ]
                                        },
                                        {
                                            "team": "Matching",
                                            "name": "Spec2",
                                            "status": "NOT_IMPLEMENTED",
                                            "tags": [ "Tag2" ],
                                            "steps": [ { "type": "GIVEN", "description": "Step2", "index": 0 } ],
                                            "interactions" : [
                                                {
                                                    "direction": "OUTBOUND",
                                                    "type": "HTTP",
                                                    "name": "Int2",
                                                    "metadata": {}
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
