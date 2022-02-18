package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.common.DummyTransactionExecutor
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.common.fixtures.FixtureConstants.COMPONENT
import io.gianluigip.spectacle.common.fixtures.FixtureConstants.SOURCE
import io.gianluigip.spectacle.common.fixtures.aFeature
import io.gianluigip.spectacle.common.fixtures.aSpec
import io.gianluigip.spectacle.common.fixtures.aTeam
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.FeatureToDelete
import io.gianluigip.spectacle.specification.model.FeatureToUpdate
import io.gianluigip.spectacle.specification.model.FeatureToUpsert
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecStatus.IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecStatus.PARTIALLY_IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecToUpsert
import io.gianluigip.spectacle.specification.model.SpecificationToUpdate
import io.gianluigip.spectacle.specification.model.SpecificationsToUpdate
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.model.TeamToDelete
import io.gianluigip.spectacle.specification.model.TeamToUpsert
import io.gianluigip.spectacle.specification.model.toTag
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.gianluigip.spectacle.specification.model.SpecificationStep as Step

private val TEAM_1 = TeamName("TEAM1")
private val TEAM_2 = TeamName("TEAM2")
private val TEAM_3 = TeamName("TEAM3")
private val FEATURE_1 = FeatureName("FEATURE1")
private val FEATURE_2 = FeatureName("FEATURE2")
private val FEATURE_3 = FeatureName("FEATURE3")
private val SPEC_1 = SpecName("SPEC1")
private val SPEC_2 = SpecName("SPEC2")
private val SPEC_3 = SpecName("SPEC3")
private val SPEC_4 = SpecName("SPEC4")

private val INTERACTION_1 = SpecInteraction(INBOUND, EVENT, "TestEvent", mapOf("meta1" to "value1"))

@Feature(name = Features.CENTRAL_REPOSITORY)
@ExtendWith(JUnitSpecificationReporter::class)
class SpecificationProcessorTest {

    private val specRepo: SpecificationRepository = mockk(relaxUnitFun = true)
    private val featureRepo: FeatureRepository = mockk(relaxUnitFun = true)
    private val teamRepo: TeamRepository = mockk(relaxUnitFun = true)
    private val transaction: TransactionExecutor = DummyTransactionExecutor()
    private val processor = SpecificationProcessor(specRepo, featureRepo, teamRepo, transaction)

    @Test
    @Specification
    fun `Detect differences when updating specs and insert, update or delete specs accordingly`() =
        given("existing specs 1, 2 and 4") {
            every { specRepo.findBy(sources = setOf(SOURCE)) } returns listOf(
                aSpec( // Spec that didn't change
                    name = SPEC_1,
                    feature = FEATURE_1,
                    team = TEAM_1,
                    source = SOURCE,
                    status = IMPLEMENTED,
                    steps = listOf(Step(type = GIVEN, "step1", index = 0))
                ),
                aSpec(
                    // Changed Spec
                    name = SPEC_2,
                ),
                aSpec(name = SPEC_4)
            )
        } and "existing features 1 and 3" run {
            every { featureRepo.findBySource(SOURCE) } returns listOf(
                aFeature(FEATURE_1, "Description 1"),
                aFeature(FEATURE_3),
            )
        } and "existing team 1 and 3" run {
            every { teamRepo.findBySource(SOURCE) } returns listOf(
                aTeam(TEAM_1), aTeam(TEAM_3)
            )
        } and "specifications update with same spec 1, changed spec 2 and new spec 3" run {
            SpecificationsToUpdate(
                source = SOURCE,
                component = COMPONENT,
                features = listOf(
                    FeatureToUpdate(
                        name = FEATURE_1, description = "Description 1", specs = listOf(
                            SpecificationToUpdate(
                                team = TEAM_1,
                                name = SPEC_1.value,
                                status = IMPLEMENTED,
                                tags = listOf("Tag1".toTag()),
                                steps = listOf(Step(type = GIVEN, "step1", index = 0)),
                                interactions = listOf(INTERACTION_1),
                            ),
                            SpecificationToUpdate(
                                team = TEAM_2,
                                name = SPEC_2.value,
                                status = PARTIALLY_IMPLEMENTED,
                                tags = listOf("Tag2".toTag()),
                                steps = listOf(Step(type = GIVEN, "step2", index = 0)),
                                interactions = listOf(INTERACTION_1),
                            )
                        )
                    ),
                    FeatureToUpdate(
                        name = FEATURE_2, description = "Description 2", specs = listOf(
                            SpecificationToUpdate(
                                team = TEAM_1,
                                name = SPEC_3.value,
                                status = IMPLEMENTED,
                                tags = listOf(),
                                steps = listOf(Step(type = GIVEN, "step3", index = 0)),
                                interactions = listOf(INTERACTION_1),
                            ),
                        )
                    )
                )
            )
        } whenever "update specifications" run { specificationsToUpdate ->
            processor.updateSpecifications(specificationsToUpdate)

        } then "spec 2 and 3 were upserted" run {
            verify {
                specRepo.upsert(
                    listOf(
                        SpecToUpsert(
                            SPEC_2, FEATURE_1, TEAM_2, SOURCE, COMPONENT, PARTIALLY_IMPLEMENTED, listOf("Tag2".toTag()),
                            steps = listOf(Step(type = GIVEN, "step2", index = 0)),
                            interactions = listOf(INTERACTION_1),
                        ),
                        SpecToUpsert(
                            SPEC_3, FEATURE_2, TEAM_1, SOURCE, COMPONENT, IMPLEMENTED, listOf(),
                            steps = listOf(Step(type = GIVEN, "step3", index = 0)),
                            interactions = listOf(INTERACTION_1),
                        )
                    )
                )
            }
        } and "spec 4 was deleted" run {
            verify { specRepo.delete(listOf(aSpec(name = SPEC_4))) }
        } and "feature 2 was upserted" run {
            verify { featureRepo.upsert(listOf(FeatureToUpsert(FEATURE_2, "Description 2", SOURCE, COMPONENT))) }
        } and "feature 3 was deleted" run {
            verify { featureRepo.delete(listOf(FeatureToDelete(FEATURE_3, SOURCE))) }
        } and "team 2 was upserted" run {
            verify { teamRepo.upsert(listOf(TeamToUpsert(TEAM_2, SOURCE, COMPONENT))) }
        } and "team 3 was deleted" runAndFinish {
            verify { teamRepo.delete(listOf(TeamToDelete(TEAM_3, SOURCE))) }
        }
}