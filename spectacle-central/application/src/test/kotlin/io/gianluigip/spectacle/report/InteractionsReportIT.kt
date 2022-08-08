package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features.SYSTEM_DIAGRAM
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.common.utils.api.getInteractionReport
import io.gianluigip.spectacle.common.utils.api.putSpecs
import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.assertions.shouldHasSize
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.specification.api.model.FeatureToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.InteractionType.PERSISTENCE
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecStatus.IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecStatus.NOT_IMPLEMENTED
import org.junit.jupiter.api.Test
import io.gianluigip.spectacle.report.api.model.SystemInteractionResponse as InteractionResponse

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
private const val TAG_1 = "Tag1"
private const val TAG_2 = "Tag2"

@Feature(
    name = SYSTEM_DIAGRAM, description = """
   Generate a diagram of the system including all the interaction registered in the specs. 
"""
)
@SpecTags(Tags.SYSTEM_DIAGRAM)
class InteractionsReportIT : BaseIntegrationTest() {

    @Test
    @Specification
    fun `Search system interactions by multiple parameters`() =
        given("existing interactions from multiple sources") {
            val source1 = SpecificationsToUpdateRequest(
                source = SOURCE_1, component = COMPONENT_1, features = listOf(
                    FeatureToUpdateRequest(
                        name = FEATURE_1, description = "", specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_1, name = SPEC_1, status = IMPLEMENTED, tags = listOf(TAG_1), steps = listOf(), interactions = listOf(
                                    SpecInteraction(INBOUND, HTTP, "Int1", metadata = mapOf("m1-1" to "v1-1")),
                                    SpecInteraction(INBOUND, EVENT, "Int2"),
                                )
                            ),
                            SpecificationToUpdateRequest(
                                TEAM_1, name = SPEC_2, status = IMPLEMENTED, tags = listOf(TAG_1), steps = listOf(), interactions = listOf(
                                    SpecInteraction(INBOUND, HTTP, "Int1", metadata = mapOf("m1-2" to "v1-2")),
                                    SpecInteraction(INBOUND, EVENT, "Int2", metadata = mapOf("m2" to "v2")),
                                    SpecInteraction(OUTBOUND, PERSISTENCE, "Int3"),
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
                                TEAM_2, name = SPEC_3, status = NOT_IMPLEMENTED, tags = listOf(TAG_2), steps = listOf(), interactions = listOf(
                                    SpecInteraction(INBOUND, HTTP, "Int4"),
                                    SpecInteraction(INBOUND, HTTP, "Int5", metadata = mapOf("m5" to "v5"))
                                )
                            )
                        )
                    ),
                )
            )
            putSpecs(source1)
            putSpecs(source2)
        } whenever "request a report for all the interactions" run {
            getInteractionReport()
        } then "the report should contain all the interactions from all the sources" run { report ->
            report assertThat {
                interactions.sortedBy { it.interactionName } assertThat {
                    shouldHasSize(5)
                    get(0) shouldBe InteractionResponse(COMPONENT_1, "Int1", INBOUND, HTTP, mapOf("m1-1" to "v1-1", "m1-2" to "v1-2"))
                    get(1) shouldBe InteractionResponse(COMPONENT_1, "Int2", INBOUND, EVENT, mapOf("m2" to "v2"))
                    get(2) shouldBe InteractionResponse(COMPONENT_1, "Int3", OUTBOUND, PERSISTENCE, emptyMap())
                    get(3) shouldBe InteractionResponse(COMPONENT_2, "Int4", INBOUND, HTTP, emptyMap())
                    get(4) shouldBe InteractionResponse(COMPONENT_2, "Int5", INBOUND, HTTP, mapOf("m5" to "v5"))
                }
                filters assertThat {
                    features shouldBe setOf(FEATURE_1, FEATURE_2)
                    sources shouldBe setOf(SOURCE_1, SOURCE_2)
                    components shouldBe setOf(COMPONENT_1, COMPONENT_2)
                    tags shouldBe setOf(TAG_1, TAG_2)
                    teams shouldBe setOf(TEAM_1, TEAM_2)
                }
            }
        } andWhenever "search by parameters" then "the report filters the interactions accordingly" runAndFinish {
            getInteractionReport(source = SOURCE_1).interactions.sortedBy { it.interactionName } assertThat {
                shouldHasSize(3)
                get(0) shouldBe InteractionResponse(COMPONENT_1, "Int1", INBOUND, HTTP, mapOf("m1-1" to "v1-1", "m1-2" to "v1-2"))
                get(1) shouldBe InteractionResponse(COMPONENT_1, "Int2", INBOUND, EVENT, mapOf("m2" to "v2"))
                get(2) shouldBe InteractionResponse(COMPONENT_1, "Int3", OUTBOUND, PERSISTENCE, emptyMap())
            }
            getInteractionReport(feature = FEATURE_2).interactions.sortedBy { it.interactionName } assertThat {
                shouldHasSize(2)
                get(0) shouldBe InteractionResponse(COMPONENT_2, "Int4", INBOUND, HTTP, emptyMap())
                get(1) shouldBe InteractionResponse(COMPONENT_2, "Int5", INBOUND, HTTP, mapOf("m5" to "v5"))
            }
            getInteractionReport(component = COMPONENT_1).interactions.sortedBy { it.interactionName } assertThat {
                shouldHasSize(3)
                get(0) shouldBe InteractionResponse(COMPONENT_1, "Int1", INBOUND, HTTP, mapOf("m1-1" to "v1-1", "m1-2" to "v1-2"))
                get(1) shouldBe InteractionResponse(COMPONENT_1, "Int2", INBOUND, EVENT, mapOf("m2" to "v2"))
                get(2) shouldBe InteractionResponse(COMPONENT_1, "Int3", OUTBOUND, PERSISTENCE, emptyMap())
            }
            getInteractionReport(tag = TAG_2).interactions.sortedBy { it.interactionName } assertThat {
                shouldHasSize(2)
                get(0) shouldBe InteractionResponse(COMPONENT_2, "Int4", INBOUND, HTTP, emptyMap())
                get(1) shouldBe InteractionResponse(COMPONENT_2, "Int5", INBOUND, HTTP, mapOf("m5" to "v5"))
            }
            getInteractionReport(team = TEAM_2).interactions.sortedBy { it.interactionName } assertThat {
                shouldHasSize(2)
                get(0) shouldBe InteractionResponse(COMPONENT_2, "Int4", INBOUND, HTTP, emptyMap())
                get(1) shouldBe InteractionResponse(COMPONENT_2, "Int5", INBOUND, HTTP, mapOf("m5" to "v5"))
            }
        }
}