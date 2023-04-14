package io.gianluigip.spectacle.auth.hooks

import io.gianluigip.spectacle.auth.AuthContext
import io.gianluigip.spectacle.auth.AuthenticatedUser
import io.gianluigip.spectacle.common.api.updateApiClientWithCredentials
import react.useRequiredContext

private var currentUser: AuthenticatedUser? = null

class AuthManager(
    private val contextUser: AuthenticatedUser?,
    private val onUserUpdated: (AuthenticatedUser?) -> Unit
) {
    fun updateAuthUser(user: AuthenticatedUser) {
        currentUser = user
        onUserUpdated(user)
        updateApiClientWithCredentials(user)
    }

    fun removeUser() {
        currentUser = null
        onUserUpdated(null)
        updateApiClientWithCredentials(null)
    }

    fun currentUser() = contextUser ?: currentUser
}

fun useAuthManager(): AuthManager {
    var user by useRequiredContext(AuthContext)

    return AuthManager(
        contextUser = user,
        onUserUpdated = {
            user = it
        }
    )
}
