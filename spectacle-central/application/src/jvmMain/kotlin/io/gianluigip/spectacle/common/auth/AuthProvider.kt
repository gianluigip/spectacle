package io.gianluigip.spectacle.common.auth

import io.ktor.server.auth.Principal
import io.ktor.server.auth.UserPasswordCredential

class AuthProvider(
    private val userFinder: UserFinder,
) {

    fun findUserByCredentials(credentials: UserPasswordCredential): Principal? {
        return userFinder.findUserByCredentials(credentials.name, credentials.password)?.toPrincipal()
    }

}
