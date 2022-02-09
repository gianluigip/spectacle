package io.gianluigip.spectacle.specification.model

import java.time.ZonedDateTime

data class Team(
    val name: TeamName,
    val sources: List<Source>,
    val components: List<Component>,
    val creationTime: ZonedDateTime,
    val updateTime: ZonedDateTime,
)

data class TeamToUpsert(val name: TeamName, val source: Source, val component: Component)

data class TeamToDelete(val name: TeamName, val source: Source)
