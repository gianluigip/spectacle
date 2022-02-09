package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features.CENTRAL_REPOSITORY
import io.gianluigip.spectacle.common.fixtures.FixtureConstants
import io.gianluigip.spectacle.common.fixtures.FixtureConstants.COMPONENT
import io.gianluigip.spectacle.common.fixtures.FixtureConstants.COMPONENTS
import io.gianluigip.spectacle.common.fixtures.FixtureConstants.SOURCE
import io.gianluigip.spectacle.common.fixtures.FixtureConstants.SOURCES
import io.gianluigip.spectacle.common.fixtures.TeamConstants.TEAM_NAME
import io.gianluigip.spectacle.common.utils.api.getSpecs
import io.gianluigip.spectacle.common.utils.api.putSpecs
import io.gianluigip.spectacle.common.utils.db.findAllFeatures
import io.gianluigip.spectacle.common.utils.db.findAllSpecs
import io.gianluigip.spectacle.common.utils.db.findAllTeams
import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.assertions.shouldHasSize
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.specification.api.model.FeatureToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecStatus.IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecStatus.NOT_IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecStatus.PARTIALLY_IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.StepType
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.StepType.THEN
import io.gianluigip.spectacle.specification.model.StepType.WHENEVER
import io.gianluigip.spectacle.specification.model.TeamName
import org.junit.jupiter.api.Test

@Feature(name = CENTRAL_REPOSITORY)
class RegisterSpecificationsIT : BaseIntegrationTest() {

    @Test
    @Specification
    fun `Register specs from one source`() =
        given("the source ServiceTest") {
        } whenever "ServiceTest publish all its specs" run {
            val specsToUpdate = SpecificationsToUpdateRequest(
                source = FixtureConstants.SOURCE.value,
                component = COMPONENT.value,
                features = listOf(
                    FeatureToUpdateRequest(
                        name = "First Feature", description = "First Feature Description", specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_NAME.value, name = "Spec 1", status = IMPLEMENTED, steps = listOf(
                                    SpecificationStep(GIVEN, description = "Description 1", index = 0),
                                    SpecificationStep(WHENEVER, description = "Description 2", index = 1),
                                    SpecificationStep(THEN, description = "Description 3", index = 2),
                                )
                            ),
                            SpecificationToUpdateRequest(
                                "SecondTeam", name = "Spec 2", status = NOT_IMPLEMENTED, steps = listOf(
                                    SpecificationStep(GIVEN, description = "Description 4", index = 0),
                                    SpecificationStep(StepType.AND, description = "Description 5", index = 1),
                                    SpecificationStep(WHENEVER, description = "Description 6", index = 2),
                                )
                            )
                        )
                    ),
                    FeatureToUpdateRequest(
                        name = "Second Feature", description = "Second Feature Description", specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_NAME.value, name = "Spec 3", status = PARTIALLY_IMPLEMENTED, steps = listOf(
                                    SpecificationStep(GIVEN, description = "Description 7", index = 0),
                                    SpecificationStep(WHENEVER, description = "Description 8", index = 1),
                                )
                            ),
                        )
                    )
                )
            )
            putSpecs(specsToUpdate)
        } then "all the specs were stored with the proper metadata" run {
            findAllSpecs() assertThat {
                shouldHasSize(3)
                get(0) assertThat {
                    name shouldBe SpecName("Spec 1")
                    source shouldBe SOURCE
                    component shouldBe COMPONENT
                    team shouldBe TEAM_NAME
                    feature shouldBe FeatureName("First Feature")
                    status shouldBe IMPLEMENTED
                    steps shouldBe listOf(
                        SpecificationStep(GIVEN, description = "Description 1", index = 0),
                        SpecificationStep(WHENEVER, description = "Description 2", index = 1),
                        SpecificationStep(THEN, description = "Description 3", index = 2),
                    )
                }
                get(1) assertThat {
                    name shouldBe SpecName("Spec 2")
                    source shouldBe SOURCE
                    component shouldBe COMPONENT
                    team shouldBe TeamName("SecondTeam")
                    feature shouldBe FeatureName("First Feature")
                    status shouldBe NOT_IMPLEMENTED
                    steps shouldBe listOf(
                        SpecificationStep(GIVEN, description = "Description 4", index = 0),
                        SpecificationStep(StepType.AND, description = "Description 5", index = 1),
                        SpecificationStep(WHENEVER, description = "Description 6", index = 2),
                    )
                }
                get(2) assertThat {
                    name shouldBe SpecName("Spec 3")
                    source shouldBe SOURCE
                    component shouldBe COMPONENT
                    team shouldBe TEAM_NAME
                    feature shouldBe FeatureName("Second Feature")
                    status shouldBe PARTIALLY_IMPLEMENTED
                    steps shouldBe listOf(
                        SpecificationStep(GIVEN, description = "Description 7", index = 0),
                        SpecificationStep(WHENEVER, description = "Description 8", index = 1),
                    )
                }
            }
        } and "the features were stored" run {
            findAllFeatures() assertThat {
                shouldHasSize(2)
                get(0) assertThat {
                    name.value shouldBe "First Feature"
                    description shouldBe "First Feature Description"
                    sources shouldBe SOURCES
                    components shouldBe COMPONENTS
                }
                get(1) assertThat {
                    name.value shouldBe "Second Feature"
                    description shouldBe "Second Feature Description"
                    sources shouldBe SOURCES
                    components shouldBe COMPONENTS
                }
            }
        } and "the teams were stored" run {
            findAllTeams() assertThat {
                shouldHasSize(2)
                get(0) assertThat {
                    name.value shouldBe "SecondTeam"
                    sources shouldBe SOURCES
                    components shouldBe COMPONENTS
                }
                get(1) assertThat {
                    name.value shouldBe TEAM_NAME.value
                    sources shouldBe SOURCES
                    components shouldBe COMPONENTS
                }
            }
        } andWhenever "GET all the specs" run {
            getSpecs()
        } then "the result contain all the specs" runAndFinish { specs ->
            specs assertThat {
                shouldHasSize(3)
                get(0) assertThat {
                    name shouldBe "Spec 1"
                    source shouldBe SOURCE.value
                    component shouldBe COMPONENT.value
                    team shouldBe TEAM_NAME.value
                    feature shouldBe "First Feature"
                    status shouldBe IMPLEMENTED
                    steps shouldBe listOf(
                        SpecificationStep(GIVEN, description = "Description 1", index = 0),
                        SpecificationStep(WHENEVER, description = "Description 2", index = 1),
                        SpecificationStep(THEN, description = "Description 3", index = 2),
                    )
                }
                get(1) assertThat {
                    name shouldBe "Spec 2"
                    source shouldBe SOURCE.value
                    component shouldBe COMPONENT.value
                    team shouldBe "SecondTeam"
                    feature shouldBe "First Feature"
                    status shouldBe NOT_IMPLEMENTED
                    steps shouldBe listOf(
                        SpecificationStep(GIVEN, description = "Description 4", index = 0),
                        SpecificationStep(StepType.AND, description = "Description 5", index = 1),
                        SpecificationStep(WHENEVER, description = "Description 6", index = 2),
                    )
                }
                get(2) assertThat {
                    name shouldBe "Spec 3"
                    source shouldBe SOURCE.value
                    component shouldBe COMPONENT.value
                    team shouldBe TEAM_NAME.value
                    feature shouldBe "Second Feature"
                    status shouldBe PARTIALLY_IMPLEMENTED
                    steps shouldBe listOf(
                        SpecificationStep(GIVEN, description = "Description 7", index = 0),
                        SpecificationStep(WHENEVER, description = "Description 8", index = 1),
                    )
                }
            }
        }
}