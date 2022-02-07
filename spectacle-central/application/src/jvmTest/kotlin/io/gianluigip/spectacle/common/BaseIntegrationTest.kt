package io.gianluigip.spectacle.common

import io.gianluigip.spectacle.common.utils.setEnv
import io.gianluigip.spectacle.di
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.specification.repository.ExposedFeatureRepository
import io.gianluigip.spectacle.specification.repository.ExposedSpecificationRepository
import io.gianluigip.spectacle.specification.repository.ExposedTeamRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.kodein.di.instance
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ExtendWith(JUnitSpecificationReporter::class)
abstract class BaseIntegrationTest {

    val port: Int = EmbeddedEnvironments.PORT
    val httpHost = "http://localhost:$port"
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
    }

    val specRepo by lazy { val instance by di.instance<ExposedSpecificationRepository>(); instance }
    val featureRepo by lazy { val instance by di.instance<ExposedFeatureRepository>(); instance }
    val teamRepo by lazy { val instance by di.instance<ExposedTeamRepository>();instance }

    @Container
    private val postgres = PostgreSQLContainer<Nothing>("postgres:14.1").apply {
        withUsername("spectacle")
        withPassword("spectacle")
        withDatabaseName("spectacle")
    }

    @BeforeEach
    fun initEnv() {
        EmbeddedEnvironments.start { setupDataBaseEnvVars() }
        cleanDb()
    }

    private fun cleanDb() = transaction {
        specRepo.deleteAll()
        featureRepo.deleteAll()
        teamRepo.deleteAll()
    }

    private fun setupDataBaseEnvVars() {
        val jdbcUrl = postgres.jdbcUrl.substring(0, postgres.jdbcUrl.indexOf("?"))
        setEnv(
            mapOf(
                "DATABASE_URL" to jdbcUrl,
                "DATABASE_USERNAME" to "spectacle",
                "DATABASE_PASSWORD" to "spectacle",
            )
        )
    }
}