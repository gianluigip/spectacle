package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features.SPECIFICATIONS_REPORT
import io.gianluigip.spectacle.common.Tags.SPECIFICATIONS
import io.gianluigip.spectacle.common.utils.CLOCK
import io.gianluigip.spectacle.common.utils.api.getSpecReport
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
import io.gianluigip.spectacle.specification.model.SpecStatus.IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecStatus.NOT_IMPLEMENTED
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import org.junit.jupiter.api.Test
import io.gianluigip.spectacle.specification.model.SpecificationStep as Step

private const val SPEC_1 = "Spec1"
private const val SPEC_2 = "Spec2"
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

@Feature(name = SPECIFICATIONS_REPORT, description = "Search and aggregate all the specs")
@SpecTags(SPECIFICATIONS)
class SpecReportIT : BaseIntegrationTest() {

    @Test
    @Specification
    fun `Search specs by multiple parameters`() =
        given("existing specs from multiple sources") {
            val source1 = SpecificationsToUpdateRequest(
                source = SOURCE_1, component = COMPONENT_1, features = listOf(
                    FeatureToUpdateRequest(
                        name = FEATURE_1, description = "$FEATURE_1 Description", specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_1, name = SPEC_1, status = IMPLEMENTED, tags = listOf(TAG_1), steps = listOf(Step(GIVEN, "Description 1", 0))
                            )
                        )
                    ),
                )
            )
            val source2 = SpecificationsToUpdateRequest(
                source = SOURCE_2, component = COMPONENT_2, features = listOf(
                    FeatureToUpdateRequest(
                        name = FEATURE_2, description = "$FEATURE_2 Description", specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_2, name = SPEC_2, status = NOT_IMPLEMENTED, tags = listOf(TAG_2), steps = listOf()
                            )
                        )
                    ),
                )
            )
            putSpecs(source1)
            putSpecs(source2)
        } whenever "request a report for all specs" run {
            getSpecReport()
        } then "the report should contain all the specs from all the sources" run { report ->
            report assertThat {
                features assertThat {
                    shouldHasSize(2)
                    first() assertThat {
                        name shouldBe FEATURE_1
                        description shouldBe "$FEATURE_1 Description"
                        specs.first() assertThat {
                            name shouldBe SPEC_1
                            team shouldBe TEAM_1
                            source shouldBe SOURCE_1
                            component shouldBe COMPONENT_1
                            status shouldBe IMPLEMENTED
                            tags shouldBe listOf(TAG_1)
                            steps shouldBe listOf(Step(GIVEN, "Description 1", 0))
                            creationTime.toEpochMilliseconds() shouldBe CLOCK.millis()
                            updateTime.toEpochMilliseconds() shouldBe CLOCK.millis()
                        }
                    }
                    last() assertThat {
                        name shouldBe FEATURE_2
                        description shouldBe "$FEATURE_2 Description"
                        specs.first() assertThat {
                            name shouldBe SPEC_2
                            team shouldBe TEAM_2
                            source shouldBe SOURCE_2
                            component shouldBe COMPONENT_2
                            status shouldBe NOT_IMPLEMENTED
                            tags shouldBe listOf(TAG_2)
                            steps shouldBe listOf()
                            creationTime.toEpochMilliseconds() shouldBe CLOCK.millis()
                            updateTime.toEpochMilliseconds() shouldBe CLOCK.millis()
                        }
                    }
                }
                filters assertThat {
                    features shouldBe setOf(FEATURE_1, FEATURE_2)
                    sources shouldBe setOf(SOURCE_1, SOURCE_2)
                    components shouldBe setOf(COMPONENT_1, COMPONENT_2)
                    tags shouldBe setOf(TAG_1, TAG_2)
                    teams shouldBe setOf(TEAM_1, TEAM_2)
                    statuses shouldBe setOf(IMPLEMENTED, NOT_IMPLEMENTED)
                }
            }
        } andWhenever "request a report for a specific source" run {
            getSpecReport(source = SOURCE_1)
        } then "it should contain only specs from that source" run { report ->
            report.features assertThat {
                shouldHasSize(1)
                first().specs.first().name shouldBe SPEC_1
            }
        } andWhenever "request a report for a specific feature" run {
            getSpecReport(feature = FEATURE_2)
        } then "it should contain only specs from that feature" run { report ->
            report.features assertThat {
                shouldHasSize(1)
                first().specs.first().name shouldBe SPEC_2
            }
        } andWhenever "request a report for a specific component" run {
            getSpecReport(component = COMPONENT_1)
        } then "it should contain only specs from that component" run { report ->
            report.features assertThat {
                shouldHasSize(1)
                first().specs.first().name shouldBe SPEC_1
            }
        } andWhenever "request a report for a specific tag" run {
            getSpecReport(tag = TAG_2)
        } then "it should contain only specs with that tag" run { report ->
            report.features assertThat {
                shouldHasSize(1)
                first().specs.first().name shouldBe SPEC_2
            }
        } andWhenever "request a report for a specific team" run {
            getSpecReport(team = TEAM_1)
        } then "it should contain only specs from that team" run { report ->
            report.features assertThat {
                shouldHasSize(1)
                first().specs.first().name shouldBe SPEC_1
            }
        } andWhenever "request a report for a specific status" run {
            getSpecReport(status = NOT_IMPLEMENTED)
        } then "it should contain only specs with that status" runAndFinish { report ->
            report.features assertThat {
                shouldHasSize(1)
                first().specs.first().name shouldBe SPEC_2
            }
        }
}