package io.gianluigip.spectacle.specification.api

import io.gianluigip.spectacle.common.auth.UserRole.READ
import io.gianluigip.spectacle.common.auth.UserRole.WRITE
import io.gianluigip.spectacle.common.auth.getForRole
import io.gianluigip.spectacle.common.auth.putForRole
import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.SpecificationProcessor
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger("SpecificationsRoutes")

fun Route.specificationsRoutes() {
    val specProcessor by di.instance<SpecificationProcessor>()
    val specFinder by di.instance<SpecificationFinder>()

    route("/specification") {

        putForRole(WRITE) {
            val request = call.receive<SpecificationsToUpdateRequest>()
            LOG.info("Updating specs for source ${request.source} and component ${request.component}")
            specProcessor.updateSpecifications(request.toModel())
            call.respond(HttpStatusCode.Created)
        }

        getForRole(READ) {
            val specs = specFinder.findAll().map { it.toResponse() }
            call.respond(specs)
        }
    }
}
