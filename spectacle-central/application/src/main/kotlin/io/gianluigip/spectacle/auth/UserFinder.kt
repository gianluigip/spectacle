package io.gianluigip.spectacle.auth

import io.gianluigip.spectacle.auth.model.User

interface UserFinder {
    fun findUserByCredentials(username: String, password: String): User?
}
