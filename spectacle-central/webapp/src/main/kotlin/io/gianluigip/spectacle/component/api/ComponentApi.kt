package io.gianluigip.spectacle.component.api

import io.gianluigip.spectacle.team.api.getTeams
import io.gianluigip.spectacle.team.api.model.TeamResponse

data class Component(
    val name: String,
    val teams: List<String>
)

suspend fun getComponents(): List<Component> = getTeams().extractComponents()

fun List<TeamResponse>.extractComponents(): List<Component> {
    val teams = mutableMapOf<String, MutableSet<String>>()
    forEach { team ->
        team.components.forEach { component ->
            val componentTeams = teams[component]
            if (componentTeams == null) {
                teams[component] = mutableSetOf(team.name)
            } else {
                componentTeams += team.name
            }
        }
    }
    return teams.map { (component, teams) -> Component(component, teams.sorted()) }
}
