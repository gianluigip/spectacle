package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.common.utils.fromUtc
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.team.model.Team
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.repository.tables.Teams.component
import io.gianluigip.spectacle.specification.repository.tables.Teams.creationTime
import io.gianluigip.spectacle.specification.repository.tables.Teams.name
import io.gianluigip.spectacle.specification.repository.tables.Teams.teamSource
import io.gianluigip.spectacle.specification.repository.tables.Teams.updateTime
import org.jetbrains.exposed.sql.ResultRow
import java.time.Clock

fun List<ResultRow>.toTeam(clock: Clock): Team {
    val sources = asSequence().map { Source(it[teamSource]) }.sortedBy { it.value }.toList()
    val components = asSequence().map { Component(it[component]) }.distinct().sortedBy { it.value }.toList()
    val minCreationTime = minByOrNull { it[creationTime] }!![creationTime].fromUtc(clock)
    val maxUpdateTime = maxByOrNull { it[updateTime] }!![updateTime].fromUtc(clock)
    return first().run {
        Team(
            name = TeamName(get(name)),
            creationTime = minCreationTime,
            updateTime = maxUpdateTime,
            sources = sources,
            components = components,
        )
    }
}
