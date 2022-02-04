package io.gianluigip.spectacle.specification.api

import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger("SpecificationsRoutes")

fun Route.specificationsRoutes() {
    route("/specification") {
        put {
            val request = call.receive<SpecificationsToUpdateRequest>()
            LOG.info("Requested: $request")
        }
    }
}
