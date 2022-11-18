package io.gianluigip.spectacle.component.api

import io.gianluigip.spectacle.team.api.getTeams
import io.gianluigip.spectacle.team.api.model.TeamResponse
import kotlinx.datetime.Instant

data class Component(
    val name: String,
    val teams: List<String>,
    val sources: List<String>,
    val updateTime: Instant,
)

suspend fun getComponents(): List<Component> = getTeams().extractComponents()

fun List<TeamResponse>.extractComponents(): List<Component> {

    return flatMap { team -> team.components.map { it to team } }.groupBy { it.first }.map { (component, teams) ->
        Component(
            name = component,
            teams = teams.map { it.second.name }.distinct().sorted(),
            sources = teams.flatMap { it.second.sources }.distinct().sorted(),
            updateTime = teams.maxOf { it.second.updateTime }
        )
    }
}
