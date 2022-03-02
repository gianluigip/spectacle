package io.gianluigip.spectacle.auth.api

import io.gianluigip.spectacle.auth.model.UserPrincipal
import io.gianluigip.spectacle.auth.model.UserRole
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.util.pipeline.PipelineContext

fun ApplicationCall.hasNotRoles(vararg roles: UserRole) = !hasRoles(*roles)

fun ApplicationCall.hasRoles(vararg roles: UserRole): Boolean = principal<UserPrincipal>()?.roles?.containsAll(roles.asList()) ?: false

suspend fun ApplicationCall.ensureRoleOrReject(role: UserRole): Boolean {
    return if (hasRoles(role)) {
        true
    } else {
        this.respond(HttpStatusCode.Unauthorized, "The user doesn't have the right access.")
        false
    }
}

inline fun Route.getForRole(
    requiredRole: UserRole,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
): Route = get {
    if (!call.ensureRoleOrReject(requiredRole)) return@get
    body(this)
}

inline fun Route.getForRole(
    requiredRole: UserRole,
    path: String,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
): Route = get(path) {
    if (!call.ensureRoleOrReject(requiredRole)) return@get
    body(this)
}

inline fun Route.postForRole(
    requiredRole: UserRole,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
): Route = post {
    if (!call.ensureRoleOrReject(requiredRole)) return@post
    body(this)
}

inline fun Route.postForRole(
    requiredRole: UserRole,
    path: String,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
): Route = post(path) {
    if (!call.ensureRoleOrReject(requiredRole)) return@post
    body(this)
}

inline fun Route.putForRole(
    requiredRole: UserRole,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
): Route = put {
    if (!call.ensureRoleOrReject(requiredRole)) return@put
    body(this)
}

inline fun Route.putForRole(
    requiredRole: UserRole,
    path: String,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
): Route = put(path) {
    if (!call.ensureRoleOrReject(requiredRole)) return@put
    body(this)
}

inline fun Route.deleteForRole(
    requiredRole: UserRole,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
): Route = delete {
    if (!call.ensureRoleOrReject(requiredRole)) return@delete
    body(this)
}

inline fun Route.deleteForRole(
    requiredRole: UserRole,
    path: String,
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
): Route = delete(path) {
    if (!call.ensureRoleOrReject(requiredRole)) return@delete
    body(this)
}
