package io.gianluigip.spectacle.common

import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.dsl.interactions.httpInteractionsConfig
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.specification.repository.ExposedFeatureRepository
import io.gianluigip.spectacle.specification.repository.ExposedSpecificationRepository
import io.gianluigip.spectacle.specification.repository.ExposedTeamRepository
import io.gianluigip.spectacle.wiki.repository.ExposedWikiPageRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.kodein.di.instance
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

private val postgres = PostgreSQLContainer<Nothing>("postgres:14.1").apply {
    withUsername("spectacle")
    withPassword("spectacle")
    withDatabaseName("spectacle")
}

@Testcontainers
@ExtendWith(JUnitSpecificationReporter::class)
abstract class BaseIntegrationTest {

    val port: Int = EmbeddedEnvironments.PORT
    val httpHost = "http://localhost:$port"
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = "AdminUser", password = "AdminPassword")
                }
            }
        }
    }

    val specRepo by lazy { val instance by di.instance<ExposedSpecificationRepository>(); instance }
    val featureRepo by lazy { val instance by di.instance<ExposedFeatureRepository>(); instance }
    val teamRepo by lazy { val instance by di.instance<ExposedTeamRepository>();instance }
    val wikiPageRepo by lazy { val instance by di.instance<ExposedWikiPageRepository>();instance }

    @BeforeEach
    fun initEnv() {
        EmbeddedEnvironments.start {
            postgres.start()
            serverConfig()
        }
        cleanDb()
        httpInteractionsConfig(host = httpHost, httpClient = httpClient)
    }

    private fun cleanDb() = transaction {
        specRepo.deleteAll()
        featureRepo.deleteAll()
        teamRepo.deleteAll()
        wikiPageRepo.deleteAll()
    }

    private fun serverConfig(): Map<String, String> {
        val jdbcUrl = postgres.jdbcUrl.substring(0, postgres.jdbcUrl.indexOf("?"))
        return mapOf(
            "database.url" to jdbcUrl,
            "database.username" to "spectacle",
            "database.password" to "spectacle",
            "users.admin.username" to "AdminUser",
            "users.admin.password" to "AdminPassword",
        )
    }
}