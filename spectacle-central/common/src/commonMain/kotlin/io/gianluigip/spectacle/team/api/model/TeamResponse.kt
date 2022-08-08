package io.gianluigip.spectacle.team.api.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TeamResponse(
    val name: String,
    val sources: List<String>,
    val components: List<String>,
    val creationTime: Instant,
    val updateTime: Instant,
)