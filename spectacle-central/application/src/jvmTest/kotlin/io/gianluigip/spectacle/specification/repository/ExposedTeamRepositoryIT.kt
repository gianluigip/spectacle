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
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.Team
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.model.TeamToDelete
import io.gianluigip.spectacle.specification.model.TeamToUpsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime


private val TEAM_1 = TeamName("Team1")
private val TEAM_2 = TeamName("Team2")
private val TEAM_3 = TeamName("Team3")
private val SOURCE_1 = Source("Source1")
private val SOURCE_2 = Source("Source2")
private val COMPONENT_1 = Component("Component1")
private val COMPONENT_2 = Component("Component2")

class ExposedTeamRepositoryIT : BaseIntegrationTest() {

    private val creationTime1 = CLOCK.toZonedDateTime()
    private lateinit var creationTime2: ZonedDateTime

    @Test
    fun `The CRUD operations should work as expected`() = transaction {
        whenever("upserting teams that doesn't exist") {
            teamRepo.upsert(
                listOf(
                    TeamToUpsert(TEAM_1, SOURCE_1, COMPONENT_1),
                    TeamToUpsert(TEAM_2, SOURCE_2, COMPONENT_2),
                )
            )
        } then "it should create all the teams" run {
            teamRepo.findAll() assertThat {
                shouldHasSize(2)
                get(TEAM_1) assertThat {
                    sources shouldBe listOf(SOURCE_1)
                    components shouldBe listOf(COMPONENT_1)
                    creationTime shouldBeCloseTo creationTime1
                    updateTime shouldBeCloseTo creationTime1
                }
                get(TEAM_2) assertThat {
                    sources shouldBe listOf(SOURCE_2)
                    components shouldBe listOf(COMPONENT_2)
                    creationTime shouldBeCloseTo creationTime1
                    updateTime shouldBeCloseTo creationTime1
                }
            }
        } andWhenever "searching the teams for source 1" run {
            teamRepo.findBySource(SOURCE_1)

        } then "it should only find team 1" run { teamsBySource ->
            teamsBySource assertThat {
                shouldHasSize(1)
                first().name shouldBe TEAM_1
            }
        } andWhenever "receive teams to update and to insert" run {
            CLOCK.forwardMinutes(10)
            creationTime2 = CLOCK.toZonedDateTime()
            teamRepo.upsert(
                listOf(
                    TeamToUpsert(TEAM_1, SOURCE_2, COMPONENT_2),
                    TeamToUpsert(TEAM_2, SOURCE_2, COMPONENT_2),
                    TeamToUpsert(TEAM_3, SOURCE_2, COMPONENT_2),
                )
            )
        } then "it should create missing teams and update existing one" run {
            teamRepo.findAll() assertThat {
                shouldHasSize(3)
                get(TEAM_1) assertThat {
                    sources shouldBe listOf(SOURCE_1, SOURCE_2)
                    components shouldBe listOf(COMPONENT_1, COMPONENT_2)
                    creationTime shouldBeCloseTo creationTime1
                    updateTime shouldBeCloseTo creationTime2
                }
                get(TEAM_2) assertThat {
                    sources shouldBe listOf(SOURCE_2)
                    components shouldBe listOf(COMPONENT_2)
                    creationTime shouldBeCloseTo creationTime1
                    updateTime shouldBeCloseTo creationTime2
                }
                get(TEAM_3) assertThat {
                    sources shouldBe listOf(SOURCE_2)
                    components shouldBe listOf(COMPONENT_2)
                    creationTime shouldBeCloseTo creationTime2
                    updateTime shouldBeCloseTo creationTime2
                }
            }
        } andWhenever "delete team 1 source 1 and team 2" run {
            teamRepo.delete(
                listOf(TeamToDelete(TEAM_1, SOURCE_1), TeamToDelete(TEAM_2, SOURCE_2))
            )
        } then "spec 1 source 2 and spec 3 should still exist" runAndFinish {
            teamRepo.findAll() assertThat {
                shouldHasSize(2)
                get(TEAM_1) assertThat {
                    sources shouldBe listOf(SOURCE_2)
                    components shouldBe listOf(COMPONENT_2)
                }
                get(TEAM_3) assertThat {
                    sources shouldBe listOf(SOURCE_2)
                    components shouldBe listOf(COMPONENT_2)
                }
            }
        }
    }

    private fun List<Team>.get(teamName: TeamName) = first { it.name.value == teamName.value }
}
