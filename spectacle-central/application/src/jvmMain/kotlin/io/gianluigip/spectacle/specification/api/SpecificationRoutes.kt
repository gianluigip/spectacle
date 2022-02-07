package io.gianluigip.spectacle.specification.api

import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.SpecificationProcessor
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger("SpecificationsRoutes")

fun Route.specificationsRoutes() {
    val specProcessor by di.instance<SpecificationProcessor>()
    val specFinder by di.instance<SpecificationFinder>()

    route("/specification") {

        put {
            val request = call.receive<SpecificationsToUpdateRequest>()
            specProcessor.updateSpecifications(request.toModel())
        }

        get {
            val specs = specFinder.findAll().map { it.toResponse() }
            call.respond(specs)
        }
    }
}
