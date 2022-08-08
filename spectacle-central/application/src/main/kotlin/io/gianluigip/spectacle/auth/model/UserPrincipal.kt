package io.gianluigip.spectacle.auth.model

import io.ktor.server.auth.Principal

data class UserPrincipal(
    val name: String,
    val username: String,
    val roles: Set<UserRole>,
) : Principal

fun User.toPrincipal() = UserPrincipal(name, username, roles)

data class User(
    val name: String,
    val username: String,
    val password: String,
    val roles: Set<UserRole>,
)
