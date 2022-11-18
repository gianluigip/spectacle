package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.common.utils.CLOCK
import io.gianluigip.spectacle.common.utils.now
import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.assertions.shouldHasSize
import io.gianluigip.spectacle.dsl.assertions.shouldNotBe
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.dsl.bdd.whenever
import io.gianluigip.spectacle.feature.model.FeatureToUpsert
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.InteractionType.LIBRARY
import io.gianluigip.spectacle.specification.model.InteractionType.PERSISTENCE
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecStatus.IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecStatus.NOT_IMPLEMENTED
import io.gianluigip.spectacle.specification.model.SpecToUpsert
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.StepType.AND
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.StepType.THEN
import io.gianluigip.spectacle.specification.model.StepType.WHENEVER
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.repository.tables.Specifications
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.util.UUID
import io.gianluigip.spectacle.specification.model.SpecInteraction as Interaction
import io.gianluigip.spectacle.specification.model.SpecificationStep as Step
import io.gianluigip.spectacle.specification.repository.tables.SpecificationSteps as Steps

private val FEATURE_1 = FeatureName("Feature1")
private val FEATURE_2 = FeatureName("Feature2")
private val TEAM_1 = TeamName("Team1")
private val TEAM_2 = TeamName("Team2")
private val SOURCE_1 = Source("Source1")
private val SOURCE_2 = Source("Source2")
private val COMPONENT_1 = Component("Component1")
private val COMPONENT_2 = Component("Component2")
private val TAG_1 = TagName("Tag1")
private val TAG_2 = TagName("Tag2")

class ExposedSpecificationRepositoryIT : BaseIntegrationTest() {

    @Test
    fun `The CRUD operations should work as expected`() = transaction {
        whenever("upserting specs that doesn't exist") {
            specRepo.upsert(
                listOf(
                    SpecToUpsert(
                        SpecName("Spec1"), FEATURE_1, TEAM_1, SOURCE_1, COMPONENT_1, NOT_IMPLEMENTED, listOf(TAG_1), listOf(
                            Step(GIVEN, "Desc1", 0), Step(WHENEVER, "Desc2", 1)
                        ), interactions = listOf(Interaction(INBOUND, HTTP, "interaction1", mapOf("m1" to "v1")))
                    ),
                    SpecToUpsert(
                        SpecName("Spec2"), FEATURE_2, TEAM_2, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(TAG_2), listOf(Step(GIVEN, "Desc1", 0)),
                        interactions = listOf(Interaction(OUTBOUND, EVENT, "interaction2", mapOf("m2" to "v2")))
                    )
                )
            )
        } then "it should create all the specs" run {
            specRepo.findAll() assertThat {
                shouldHasSize(2)
                get("Spec1", SOURCE_1) assertThat {
                    component shouldBe COMPONENT_1
                    feature shouldBe FEATURE_1
                    team shouldBe TEAM_1
                    status shouldBe NOT_IMPLEMENTED
                    tags shouldBe listOf(TAG_1)
                    steps shouldBe listOf(Step(GIVEN, "Desc1", 0), Step(WHENEVER, "Desc2", 1))
                    interactions shouldBe listOf(Interaction(INBOUND, HTTP, "interaction1", mapOf("m1" to "v1")))
                }
                get("Spec2", SOURCE_2) assertThat {
                    component shouldBe COMPONENT_2
                    feature shouldBe FEATURE_2
                    team shouldBe TEAM_2
                    status shouldBe IMPLEMENTED
                    tags shouldBe listOf(TAG_2)
                    steps shouldBe listOf(Step(GIVEN, "Desc1", 0))
                    interactions shouldBe listOf(Interaction(OUTBOUND, EVENT, "interaction2", mapOf("m2" to "v2")))
                }
            }
        } andWhenever "searching the specs for source 1" run {
            specRepo.findBySource(SOURCE_1)

        } then "it should only find spec 1" run { specsBySource ->
            specsBySource assertThat {
                shouldHasSize(1)
                first().name shouldBe SpecName("Spec1")
            }
        } andWhenever "receive specs to update and to insert" run {

            specRepo.upsert(
                listOf(
                    SpecToUpsert(
                        SpecName("Spec1"), FEATURE_2, TEAM_2, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(TAG_2), listOf(Step(AND, "DescChanged", 0)),
                        interactions = listOf(Interaction(OUTBOUND, PERSISTENCE, "interaction1-2", mapOf("m1-2" to "v1-2")))
                    ),
                    SpecToUpsert(
                        SpecName("Spec2"), FEATURE_1, TEAM_1, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(), listOf(Step(THEN, "DescChanged", 0)),
                        interactions = listOf(Interaction(OUTBOUND, EVENT, "interaction2-2", mapOf("m2-2" to "v2-2")))
                    ),
                    SpecToUpsert(
                        SpecName("Spec3"), FEATURE_2, TEAM_2, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(TAG_1, TAG_2),
                        listOf(Step(GIVEN, "Desc3", 0)),
                        interactions = listOf(Interaction(OUTBOUND, EVENT, "interaction3", mapOf("m3" to "v3")))
                    ),
                )
            )
        } then "it should create missing spec and update existing one" run {
            specRepo.findAll() assertThat {
                shouldHasSize(4)
                get("Spec1", SOURCE_1) assertThat {
                    component shouldBe COMPONENT_1
                    feature shouldBe FEATURE_1
                    team shouldBe TEAM_1
                    status shouldBe NOT_IMPLEMENTED
                    tags shouldBe listOf(TAG_1)
                    steps shouldBe listOf(Step(GIVEN, "Desc1", 0), Step(WHENEVER, "Desc2", 1))
                    interactions shouldBe listOf(Interaction(INBOUND, HTTP, "interaction1", mapOf("m1" to "v1")))
                }
                get("Spec1", SOURCE_2) assertThat {
                    component shouldBe COMPONENT_2
                    feature shouldBe FEATURE_2
                    team shouldBe TEAM_2
                    status shouldBe IMPLEMENTED
                    tags shouldBe listOf(TAG_2)
                    steps shouldBe listOf(Step(AND, "DescChanged", 0))
                    interactions shouldBe listOf(Interaction(OUTBOUND, PERSISTENCE, "interaction1-2", mapOf("m1-2" to "v1-2")))
                }
                get("Spec2", SOURCE_2) assertThat {
                    component shouldBe COMPONENT_2
                    feature shouldBe FEATURE_1
                    team shouldBe TEAM_1
                    status shouldBe IMPLEMENTED
                    tags shouldBe listOf()
                    steps shouldBe listOf(Step(THEN, "DescChanged", 0))
                    interactions shouldBe listOf(Interaction(OUTBOUND, EVENT, "interaction2-2", mapOf("m2-2" to "v2-2")))
                }
                get("Spec3", SOURCE_2) assertThat {
                    component shouldBe COMPONENT_2
                    feature shouldBe FEATURE_2
                    team shouldBe TEAM_2
                    status shouldBe IMPLEMENTED
                    tags shouldBe listOf(TAG_1, TAG_2)
                    steps shouldBe listOf(Step(GIVEN, "Desc3", 0))
                    interactions shouldBe listOf(Interaction(OUTBOUND, EVENT, "interaction3", mapOf("m3" to "v3")))
                }
            }
        } andWhenever "delete spec 1 source 1 and spec 2" run {
            specRepo.delete(
                listOf(
                    Specification(SpecName("Spec1"), FEATURE_2, TEAM_2, SOURCE_1, COMPONENT_1, IMPLEMENTED, listOf(), listOf(), listOf()),
                    Specification(SpecName("Spec2"), FEATURE_1, TEAM_1, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(), listOf(), listOf()),
                    Specification(SpecName("Spec3"), FEATURE_1, TEAM_1, SOURCE_1, COMPONENT_1, IMPLEMENTED, listOf(), listOf(), listOf()),
                )
            )
        } then "spec 1 source 2 and spec 3 should still exist" runAndFinish {
            specRepo.findAll() assertThat {
                shouldHasSize(2)
                get("Spec1", SOURCE_2) shouldNotBe null
                get("Spec3", SOURCE_2) shouldNotBe null
            }
        }
    }

    @Test
    fun `findBy should filter the specs properly`() = transaction {
        given("Existing specs") {
            specRepo.upsert(
                listOf(
                    SpecToUpsert(
                        SpecName("Spec1"), FEATURE_1, TEAM_1, SOURCE_1, COMPONENT_1, NOT_IMPLEMENTED, listOf(TAG_1), listOf(Step(GIVEN, "Desc1", 0)),
                        interactions = listOf(Interaction(INBOUND, EVENT, "event1"))
                    ),
                    SpecToUpsert(
                        SpecName("Spec2"), FEATURE_2, TEAM_2, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(TAG_2), listOf(Step(GIVEN, "Desc2", 0)),
                        interactions = listOf(Interaction(INBOUND, LIBRARY, "lib2"))
                    ),
                )
            )
            featureRepo.upsert(
                listOf(
                    FeatureToUpsert(name = FEATURE_1, description = "Feature 1 Desc", source = SOURCE_1, component = COMPONENT_1),
                    FeatureToUpsert(name = FEATURE_2, description = "Feature 2 Desc", source = SOURCE_1, component = COMPONENT_1),
                )
            )
            CLOCK.forwardMinutes(5)
            specRepo.upsert(
                listOf(
                    SpecToUpsert(SpecName("Spec3"), FEATURE_2, TEAM_2, SOURCE_2, COMPONENT_2, IMPLEMENTED, listOf(TAG_1), listOf(), emptyList())
                )
            )
        } whenever "search by feature 1" run {
            specRepo.findBy(features = setOf(FEATURE_2))
        } then "it should return spec 2 and 3" run { specs ->
            specs assertThat {
                shouldHasSize(2)
                get("Spec2", SOURCE_2) shouldNotBe null
                get("Spec3", SOURCE_2) shouldNotBe null
            }
        } andWhenever "search by source 1" run {
            specRepo.findBy(sources = setOf(SOURCE_1))
        } then "it should return spec 1" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec1", SOURCE_1) shouldNotBe null
            }
        } andWhenever "search by component 1" run {
            specRepo.findBy(components = setOf(COMPONENT_1))
        } then "it should return spec 1" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec1", SOURCE_1) shouldNotBe null
            }
        } andWhenever "search by team 2" run {
            specRepo.findBy(teams = setOf(TEAM_2))
        } then "it should return spec 2 and 3" run { specs ->
            specs assertThat {
                shouldHasSize(2)
                get("Spec2", SOURCE_2) shouldNotBe null
                get("Spec3", SOURCE_2) shouldNotBe null
            }
        } andWhenever "search by status NOT_IMPLEMENTED" run {
            specRepo.findBy(statuses = setOf(NOT_IMPLEMENTED))
        } then "it should return spec 1" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec1", SOURCE_1) shouldNotBe null
            }
        } andWhenever "search by tag 2" run {
            specRepo.findBy(tags = setOf(TAG_2))
        } then "it should return spec 2" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec2", SOURCE_2) shouldNotBe null
            }
        } andWhenever "search by text using feature name 'Feature2'" run {
            specRepo.findBy(searchText = "Feature2")
        } then "it should return spec 2 and 3" run { specs ->
            specs assertThat {
                shouldHasSize(2)
                get("Spec2", SOURCE_2) shouldNotBe null
                get("Spec3", SOURCE_2) shouldNotBe null
            }
        } andWhenever "search by text with spec name 'ec1'" run {
            specRepo.findBy(searchText = "ec1")
        } then "it should return spec 1" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec1", SOURCE_1) shouldNotBe null
            }
        } andWhenever "search by text with description 'desc2'" run {
            specRepo.findBy(searchText = "desc2")
        } then "it should return spec 2" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec2", SOURCE_2) shouldNotBe null
            }
        } andWhenever "search by text with feature description 'Feature 1 Desc'" run {
            specRepo.findBy(searchText = "Feature 1 Desc")
        } then "it should return spec 2" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec1", SOURCE_1) shouldNotBe null
            }
        } andWhenever "search by updatedTimeAfter" run {
            specRepo.findBy(updatedTimeAfter = Instant.fromEpochMilliseconds(CLOCK.millis()))
        } then "it should return only the latest updated specs" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec3", SOURCE_2) shouldNotBe null
            }
        } andWhenever "search by interaction type" run {
            specRepo.findBy(interactionType = EVENT)
        } then "it should return only the specified type" run { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec1", SOURCE_1) shouldNotBe null
            }
        } andWhenever "search by interaction name" run {
            specRepo.findBy(interactionName = "b2")
        } then "it should return only the latest updated specs" runAndFinish { specs ->
            specs assertThat {
                shouldHasSize(1)
                get("Spec2", SOURCE_2) shouldNotBe null
            }
        }

    }

    @Test
    fun `New columns should be backward compatible`() = transaction {
        given("a spec record without the latest columns") {
            val specId = UUID.randomUUID().toString()
            Specifications.insert {
                it[id] = specId
                it[creationTime] = now().toLocalDateTime()
                it[updateTime] = now().toLocalDateTime()
                it[name] = "Spec1"
                it[feature] = "Feature1"
                it[team] = "Team1"
                it[specSource] = "Source1"
                it[component] = "Component1"
                it[status] = ""
            }
            Steps.insert {
                it[Steps.id] = UUID.randomUUID().toString()
                it[Steps.creationTime] = now().toLocalDateTime()
                it[Steps.updateTime] = now().toLocalDateTime()
                it[Steps.specId] = specId
                it[Steps.type] = WHENEVER.name
                it[Steps.description] = ""
                it[Steps.index] = 0
            }
        } whenever "search for the specs" run {
            specRepo.findAll()
        } then "it should map properly and use default values" runAndFinish { specs ->
            specs.first() assertThat {
                status shouldBe IMPLEMENTED
                tags shouldBe listOf()
            }
        }
    }

    private fun List<Specification>.get(specName: String, source: Source) = first { it.name.value == specName && it.source == source }
}
