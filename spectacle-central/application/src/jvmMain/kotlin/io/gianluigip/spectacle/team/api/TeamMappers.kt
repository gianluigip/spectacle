package io.gianluigip.spectacle.team.api

import io.gianluigip.spectacle.common.utils.toKotlinInstant
import io.gianluigip.spectacle.team.model.Team
import io.gianluigip.spectacle.team.api.model.TeamResponse

fun Team.toResponse() = TeamResponse(
    name = name.value,
    sources = sources.map { it.value }.sorted(),
    components = components.map { it.value }.sorted(),
    creationTime = creationTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
)