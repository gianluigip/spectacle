package io.gianluigip.spectacle.auth

import io.gianluigip.spectacle.auth.model.User
import io.gianluigip.spectacle.auth.model.UserRole.*
import io.ktor.server.config.*
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger("EnvVarsUserFinder")

class EnvVarsUserFinder(
    private val config: ApplicationConfig
) : UserFinder {

    private val users: Set<User> = findEnvVarsUsers()

    override fun findUserByCredentials(username: String, password: String): User? =
        users.find { it.username == username && it.password == password }

    private fun findEnvVarsUsers(): Set<User> {
        val users = mutableSetOf<User>()
        if (config.propertyOrNull("users.admin.username") == null) {
            LOG.warn("The env var ADMIN_USERNAME is missing, there isn't an Admin user.")
        } else {
            LOG.info("Registering Admin user from env vars.")
            users += User(
                name = "Admin",
                username = config.propertyOrNull("users.admin.username")!!.getString(),
                password = config.propertyOrNull("users.admin.password")?.getString() ?: "",
                roles = setOf(READ, WRITE, ADMIN)
            )
        }
        config.propertyOrNull("users.guest.username")?.getString()?.let { questUsername ->
            LOG.info("Registering Guest user from env vars.")
            users += User(
                name = "Guest",
                username = questUsername,
                password = config.propertyOrNull("users.guest.password")?.getString() ?: "",
                roles = setOf(READ)
            )
        }
        return users
    }
}
