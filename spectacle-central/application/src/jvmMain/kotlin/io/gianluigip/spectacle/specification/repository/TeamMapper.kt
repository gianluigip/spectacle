package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.Team
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.repository.tables.Teams.creationTime
import io.gianluigip.spectacle.specification.repository.tables.Teams.name
import io.gianluigip.spectacle.specification.repository.tables.Teams.teamSource
import io.gianluigip.spectacle.specification.repository.tables.Teams.updateTime
import org.jetbrains.exposed.sql.ResultRow
import java.time.Clock

fun List<ResultRow>.toTeam(clock: Clock): Team {
    val sources = map { Source(it[teamSource]) }
    val minCreationTime = minByOrNull { it[creationTime] }!![creationTime].atZone(clock.zone)
    val maxUpdateTime = maxByOrNull { it[updateTime] }!![updateTime].atZone(clock.zone)
    return first().run {
        Team(
            name = TeamName(get(name)),
            creationTime = minCreationTime,
            updateTime = maxUpdateTime,
            sources = sources,
        )
    }
}
