package io.gianluigip.spectacle.specification.model

import java.time.ZonedDateTime

@JvmInline
value class TeamName(val value: String) {
    override fun toString(): String {
        return value
    }
}

fun String.toTeam() = TeamName(this)

data class Team(
    val name: TeamName,
    val sources: List<Source>,
    val creationTime: ZonedDateTime,
    val updateTime: ZonedDateTime,
)

data class TeamToUpsert(val name: TeamName, val source: Source)

data class TeamToDelete(val name: TeamName, val source: Source)
