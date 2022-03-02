package io.gianluigip.spectacle.auth.api.model

import io.gianluigip.spectacle.auth.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)

@Serializable
data class AuthenticatedUserResponse(
    val name: String,
    val username: String,
    val roles: Set<UserRole>,
)
