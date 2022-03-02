package io.gianluigip.spectacle.feature.api

import io.gianluigip.spectacle.common.auth.UserRole.READ
import io.gianluigip.spectacle.common.auth.getForRole
import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.feature.FeatureFinder
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.kodein.di.instance

fun Route.featuresRoutes() {
    val featureRepo by di.instance<FeatureFinder>()

    route("/features") {
        getForRole(READ) {
            val specs = featureRepo.findAll().map { it.toResponse() }
            call.respond(specs)
        }
    }
}