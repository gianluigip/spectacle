package io.gianluigip.spectacle.common.auth

import io.gianluigip.spectacle.common.auth.UserRole.ADMIN
import io.gianluigip.spectacle.common.auth.UserRole.READ
import io.gianluigip.spectacle.common.auth.UserRole.WRITE
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger("EnvVarsUserFinder")

class EnvVarsUserFinder : UserFinder {

    private val users: Set<User> = findEnvVarsUsers()

    override fun findUserByCredentials(username: String, password: String): User? =
        users.find { it.username == username && it.password == password }

    private fun findEnvVarsUsers(): Set<User> {
        val users = mutableSetOf<User>()
        val envs = System.getenv()
        if (!envs.containsKey("ADMIN_USERNAME")) {
            LOG.warn("The env var ADMIN_USERNAME is missing, there isn't an Admin user.")
        } else {
            LOG.info("Registering Admin user from env vars.")
            users += User(
                username = envs["ADMIN_USERNAME"]!!,
                password = envs["ADMIN_PASSWORD"] ?: "",
                roles = setOf(READ, WRITE, ADMIN)
            )
        }
        envs["GUEST_USERNAME"]?.let { questUsername ->
            LOG.info("Registering Guest user from env vars.")
            users += User(
                username = questUsername,
                password = envs["GUEST_PASSWORD"] ?: "",
                roles = setOf(READ)
            )
        }
        return users
    }
}
