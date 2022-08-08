package io.gianluigip.spectacle.team.api

import io.gianluigip.spectacle.auth.model.UserRole.READ
import io.gianluigip.spectacle.auth.api.getForRole
import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.team.TeamFinder
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.kodein.di.instance

fun Route.teamRoutes() {
    val teamRepo by di.instance<TeamFinder>()

    route("/teams") {
        getForRole(READ) {
            val specs = teamRepo.findAll().map { it.toResponse() }
            call.respond(specs)
        }
    }
}