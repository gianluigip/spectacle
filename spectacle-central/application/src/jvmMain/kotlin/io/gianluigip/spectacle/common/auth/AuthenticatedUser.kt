package io.gianluigip.spectacle.common.auth

import io.ktor.server.auth.Principal

data class UserPrincipal(
    val username: String,
    val roles: Set<UserRole>,
) : Principal

fun User.toPrincipal() = UserPrincipal(username, roles)

data class User(
    val username: String,
    val password: String,
    val roles: Set<UserRole>,
)

enum class UserRole {
    READ, WRITE, ADMIN
}
