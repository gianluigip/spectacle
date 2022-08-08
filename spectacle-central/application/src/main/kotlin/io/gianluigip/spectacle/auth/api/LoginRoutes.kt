package io.gianluigip.spectacle.auth.api

import io.gianluigip.spectacle.auth.UserFinder
import io.gianluigip.spectacle.auth.api.model.AuthenticatedUserResponse
import io.gianluigip.spectacle.auth.api.model.LoginRequest
import io.gianluigip.spectacle.di
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.kodein.di.instance

fun Route.loginRoutes() {
    val userFinder by di.instance<UserFinder>()

    post("/login") {
        val request = call.receive<LoginRequest>()
        val user = userFinder.findUserByCredentials(request.username, request.password)
        if (user != null) {
            call.respond(AuthenticatedUserResponse(user.name, user.username, user.roles))
        } else call.respond(HttpStatusCode.Unauthorized)
    }
}
