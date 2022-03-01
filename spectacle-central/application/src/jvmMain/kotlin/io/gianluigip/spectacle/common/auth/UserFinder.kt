package io.gianluigip.spectacle.common.auth

interface UserFinder {
    fun findUserByCredentials(username: String, password: String): User?
}
