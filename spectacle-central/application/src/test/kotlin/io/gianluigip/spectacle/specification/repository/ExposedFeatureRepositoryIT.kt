package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.common.utils.CLOCK
import io.gianluigip.spectacle.common.utils.toZonedDateTime
import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.assertions.shouldBeCloseTo
import io.gianluigip.spectacle.dsl.assertions.shouldHasSize
import io.gianluigip.spectacle.dsl.bdd.whenever
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.feature.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.feature.model.FeatureToDelete
import io.gianluigip.spectacle.feature.model.FeatureToUpsert
import io.gianluigip.spectacle.specification.model.Source
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

private val FEATURE_1 = FeatureName("Feature1")
private val FEATURE_2 = FeatureName("Feature2")
private val FEATURE_3 = FeatureName("Feature3")
private val SOURCE_1 = Source("Source1")
private val SOURCE_2 = Source("Source2")
private val COMPONENT_1 = Component("Component1")
private val COMPONENT_2 = Component("Component2")

class ExposedFeatureRepositoryIT : BaseIntegrationTest() {

    private val creationTime1 = CLOCK.toZonedDateTime()
    private lateinit var creationTime2: ZonedDateTime

    @Test
    fun `The CRUD operations should work as expected`() = transaction {
        whenever("upserting features that doesn't exist") {
            featureRepo.upsert(
                listOf(
                    FeatureToUpsert(FEATURE_1, "Desc1", SOURCE_1, COMPONENT_1),
                    FeatureToUpsert(FEATURE_2, "Desc2", SOURCE_2, COMPONENT_2),
                )
            )
        } then "it should create all the features" run {
            featureRepo.findAll() assertThat {
                shouldHasSize(2)
                get(FEATURE_1) assertThat {
                    description shouldBe "Desc1"
                    sources shouldBe listOf(SOURCE_1)
                    components shouldBe listOf(COMPONENT_1)
                    creationTime shouldBeCloseTo creationTime1
                    updateTime shouldBeCloseTo creationTime1
                }
                get(FEATURE_2) assertThat {
                    description shouldBe "Desc2"
                    sources shouldBe listOf(SOURCE_2)
                    components shouldBe listOf(COMPONENT_2)
                    creationTime shouldBeCloseTo creationTime1
                    updateTime shouldBeCloseTo creationTime1
                }
            }
        } andWhenever "searching the features for source 1" run {
            featureRepo.findBySource(SOURCE_1)

        } then "it should only find feature 1" run { featuresBySource ->
            featuresBySource assertThat {
                shouldHasSize(1)
                first().name shouldBe FEATURE_1
            }
        } andWhenever "receive features to update and to insert" run {
            CLOCK.forwardMinutes(10)
            creationTime2 = CLOCK.toZonedDateTime()
            featureRepo.upsert(
                listOf(
                    FeatureToUpsert(FEATURE_1, "Desc1-2", SOURCE_2, COMPONENT_2),
                    FeatureToUpsert(FEATURE_2, "Desc2", SOURCE_2, COMPONENT_2),
                    FeatureToUpsert(FEATURE_3, "Desc3", SOURCE_2, COMPONENT_2),
                )
            )
        } then "it should create missing features and update existing one" run {
            featureRepo.findAll() assertThat {
                shouldHasSize(3)
                get(FEATURE_1) assertThat {
                    description shouldBe "Desc1-2"
                    sources shouldBe listOf(SOURCE_1, SOURCE_2)
                    components shouldBe listOf(COMPONENT_1, COMPONENT_2)
                    creationTime shouldBeCloseTo creationTime1
                    updateTime shouldBeCloseTo creationTime2
                }
                get(FEATURE_2) assertThat {
                    description shouldBe "Desc2"
                    sources shouldBe listOf(SOURCE_2)
                    components shouldBe listOf(COMPONENT_2)
                    creationTime shouldBeCloseTo creationTime1
                    updateTime shouldBeCloseTo creationTime2
                }
                get(FEATURE_3) assertThat {
                    description shouldBe "Desc3"
                    sources shouldBe listOf(SOURCE_2)
                    components shouldBe listOf(COMPONENT_2)
                    creationTime shouldBeCloseTo creationTime2
                    updateTime shouldBeCloseTo creationTime2
                }
            }
        } andWhenever "delete feature 1 source 1 and feature 2" run {
            featureRepo.delete(
                listOf(FeatureToDelete(FEATURE_1, SOURCE_1), FeatureToDelete(FEATURE_2, SOURCE_2))
            )
        } then "feature 1 source 2 and feature 3 should still exist" runAndFinish {
            featureRepo.findAll() assertThat {
                shouldHasSize(2)
                get(FEATURE_1) assertThat {
                    description shouldBe "Desc1-2"
                    sources shouldBe listOf(SOURCE_2)
                }
                get(FEATURE_3) assertThat {
                    description shouldBe "Desc3"
                    sources shouldBe listOf(SOURCE_2)
                }
            }
        }
    }

    @Test
    fun `findByNames should filter by name`() = transaction {
        featureRepo.upsert(
            listOf(
                FeatureToUpsert(FEATURE_1, "Desc1", SOURCE_1, COMPONENT_1),
                FeatureToUpsert(FEATURE_2, "Desc2", SOURCE_2, COMPONENT_2),
                FeatureToUpsert(FEATURE_3, "Desc3", SOURCE_2, COMPONENT_2),
            )
        )

        featureRepo.findByNames(listOf(FEATURE_1, FEATURE_3)) assertThat {
            shouldHasSize(2)
            first().name shouldBe FEATURE_1
            last().name shouldBe FEATURE_3
        }
    }

    private fun List<Feature>.get(featureName: FeatureName) = first { it.name.value == featureName.value }
}