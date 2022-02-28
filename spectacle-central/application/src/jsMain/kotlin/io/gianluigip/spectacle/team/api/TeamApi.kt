package io.gianluigip.spectacle.team.api

import io.gianluigip.spectacle.common.api.API_CLIENT
import io.gianluigip.spectacle.common.api.ENDPOINT
import io.gianluigip.spectacle.team.api.model.TeamResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun getTeams(): List<TeamResponse> = API_CLIENT.get("$ENDPOINT/api/teams").body()