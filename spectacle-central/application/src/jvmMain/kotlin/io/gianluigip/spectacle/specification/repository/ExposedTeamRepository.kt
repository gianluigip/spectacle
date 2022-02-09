package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.common.utils.toUtcLocalDateTime
import io.gianluigip.spectacle.specification.TeamRepository
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.Team
import io.gianluigip.spectacle.specification.model.TeamToDelete
import io.gianluigip.spectacle.specification.model.TeamToUpsert
import io.gianluigip.spectacle.specification.repository.tables.Teams
import io.gianluigip.spectacle.specification.repository.tables.Teams.id
import io.gianluigip.spectacle.specification.repository.tables.Teams.name
import io.gianluigip.spectacle.specification.repository.tables.Teams.teamSource
import io.gianluigip.spectacle.specification.repository.tables.searchExpression
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.compoundOr
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.Clock
import java.util.UUID

class ExposedTeamRepository(
    private val clock: Clock,
) : TeamRepository {

    fun deleteAll() {
        Teams.deleteAll()
    }

    override fun findAll(): List<Team> = Teams.selectAll().toTeams()

    override fun findBySource(source: Source): List<Team> =
        Teams.select { teamSource eq source.value }.toTeams()

    private fun Query.toTeams() = orderBy(name to SortOrder.ASC)
        .groupBy { it[name] }.values.map { it.sortedBy { it[teamSource] }.toTeam(clock) }

    override fun upsert(teams: List<TeamToUpsert>) {
        val existingTeams = mutableMapOf<String, String>()
        Teams.select { name inList teams.map { it.name.value } }
            .forEach {
                existingTeams["${it[teamSource]}-${it[name]}"] = it[id].value
            }

        teams.forEach { team ->
            val teamId: String? = existingTeams["${team.source.value}-${team.name.value}"]
            if (teamId != null) {
                updateTeam(team, teamId)
            } else {
                insertTeam(team)
            }
        }
    }

    private fun insertTeam(team: TeamToUpsert) {
        val teamId = UUID.randomUUID().toString()
        Teams.insert {
            it[id] = teamId
            it[creationTime] = clock.toUtcLocalDateTime()
            it[updateTime] = clock.toUtcLocalDateTime()
            it[name] = team.name.value
            it[teamSource] = team.source.value
            it[component] = team.component.value
        }
    }

    private fun updateTeam(team: TeamToUpsert, teamId: String) {
        Teams.update({ id eq teamId }) {
            it[updateTime] = clock.toUtcLocalDateTime()
            it[name] = team.name.value
            it[teamSource] = team.source.value
            it[component] = team.component.value
        }
    }

    override fun delete(teamsToDelete: List<TeamToDelete>) {
        if (teamsToDelete.isEmpty()) return
        val selectTeams = teamsToDelete.map { it.searchExpression() }.compoundOr()
        Teams.deleteWhere { selectTeams }
    }
}
