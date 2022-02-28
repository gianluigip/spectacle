package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.DummyTransactionExecutor
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.common.fixtures.aFeature
import io.gianluigip.spectacle.common.fixtures.aSpec
import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.assertions.shouldHasSize
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.report.model.SpecsReport
import io.gianluigip.spectacle.specification.FeatureRepository
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecStatus.IMPLEMENTED
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.gianluigip.spectacle.specification.model.SpecificationStep as Step

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

@Feature(name = Features.SPECIFICATIONS)
@SpecTags(Tags.SPECIFICATIONS)
@ExtendWith(JUnitSpecificationReporter::class)
class SpecReportGeneratorTest {

    private val specFinder: SpecificationFinder = mockk()
    private val featureRepo: FeatureRepository = mockk()
    private val reportGenerator = SpecReportGenerator(specFinder, featureRepo, DummyTransactionExecutor())

    private lateinit var report: SpecsReport

    @Test
    @Specification
    fun `Generate a report including all the specs from multiple features and sources`() =
        given("existing specs for multiple features from several sources") {
            every {
                specFinder.findBy()
            } returns listOf(
                aSpec(SpecName("Spec1"), FEATURE_1, TEAM_1, SOURCE_1, COMPONENT_1, IMPLEMENTED, listOf(TAG_1), steps = listOf(Step(GIVEN, "", 0))),
                aSpec(SpecName("Spec2"), FEATURE_1, TEAM_2, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(TAG_2), steps = listOf(Step(GIVEN, "", 0))),
                aSpec(SpecName("Spec3"), FEATURE_2, TEAM_2, SOURCE_1, COMPONENT_1, IMPLEMENTED, listOf(TAG_1), steps = listOf(Step(GIVEN, "", 0))),
                aSpec(SpecName("Spec4"), FEATURE_2, TEAM_2, SOURCE_1, COMPONENT_1, IMPLEMENTED, listOf(TAG_1), steps = listOf(Step(GIVEN, "", 0))),
                aSpec(SpecName("Spec5"), FEATURE_3, TEAM_1, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(TAG_2), steps = listOf(Step(GIVEN, "", 0))),
            )
            every { featureRepo.findByNames(setOf(FEATURE_1, FEATURE_2, FEATURE_3)) } returns listOf(
                aFeature(FEATURE_1, description = "Description Feature 1"),
                aFeature(FEATURE_2, description = "Description Feature 2"),
            )
        } whenever "generate a specification report" run {
            report = reportGenerator.generateReport()

        } then "the report aggregate all the specs by features and include all the sources" run {
            report.features assertThat {
                shouldHasSize(3)
                get(0) assertThat {
                    name shouldBe FEATURE_1
                    description shouldBe "Description Feature 1"
                    specs assertThat {
                        shouldHasSize(2)
                        get(0) assertThat {
                            name shouldBe SpecName("Spec1")
                            tags shouldBe listOf(TAG_1)
                            steps shouldBe listOf(Step(GIVEN, "", 0))
                        }
                        get(1) assertThat {
                            name shouldBe SpecName("Spec2")
                            tags shouldBe listOf(TAG_2)
                        }
                    }
                }
                get(1) assertThat {
                    name shouldBe FEATURE_2
                    description shouldBe "Description Feature 2"
                    specs assertThat {
                        shouldHasSize(2)
                        get(0).name shouldBe SpecName("Spec3")
                        get(1).name shouldBe SpecName("Spec4")
                    }
                }
                get(2) assertThat {
                    name shouldBe FEATURE_3
                    description shouldBe ""
                    specs assertThat {
                        shouldHasSize(1)
                        get(0).name shouldBe SpecName("Spec5")
                    }
                }
            }
        } and "it should include all the filters" runAndFinish {
            report.filters assertThat {
                features shouldBe setOf(FEATURE_1, FEATURE_2, FEATURE_3)
                sources shouldBe setOf(SOURCE_1, SOURCE_2)
                components shouldBe setOf(COMPONENT_1, COMPONENT_2)
                tags shouldBe setOf(TAG_1, TAG_2)
                teams shouldBe setOf(TEAM_1, TEAM_2)
                statuses shouldBe setOf(IMPLEMENTED)
            }
        }
}
