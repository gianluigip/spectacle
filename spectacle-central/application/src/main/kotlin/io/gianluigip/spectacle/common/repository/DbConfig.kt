package io.gianluigip.spectacle.common.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import javax.sql.DataSource

private val LOG = LoggerFactory.getLogger("DbConfig")
private lateinit var dataSource: DataSource

fun initDb(config: ApplicationConfig) {
    dataSource = buildDataSourceWithEnv(config)
    Database.connect(dataSource)
    initFlyway()
}

private fun buildDataSourceWithEnv(config: ApplicationConfig): DataSource {

    var dbHost = ""
    var port = 0
    var database = ""
    var username: String? = null
    var password: String? = null

    config.propertyOrNull("database.url")?.let { dbUrlProperty ->
        var dbUrl = dbUrlProperty.getString()
        if (dbUrl.isBlank()) return@let

        if (dbUrl.contains("://")) {
            dbUrl = dbUrl.substring(dbUrl.indexOf("://") + 3)
        }
        if (dbUrl.contains("@")) {
            username = dbUrl.substring(0, dbUrl.indexOf(":"))
            password = dbUrl.substring(dbUrl.indexOf(":") + 1, dbUrl.indexOf("@"))
            dbUrl = dbUrl.substring(dbUrl.indexOf("@") + 1)
        }
        val hostSegments = dbUrl.split("/")
        dbHost = hostSegments.first().split(":").first()
        port = hostSegments.first().split(":").last().toInt()
        database = hostSegments.last()
    }

    return buildDataSource(
        dbHost = config.propertyOrNull("database.host")?.getString() ?: dbHost,
        port = config.propertyOrNull("database.port")?.getString()?.toInt() ?: port,
        database = config.propertyOrNull("database.dbName")?.getString() ?: database,
        username = config.propertyOrNull("database.username")?.getString() ?: username,
        password = config.propertyOrNull("database.password")?.getString() ?: password
    )
}

private fun Map<String, String>.getEnv(value: String) =
    if (get(value)?.isNotEmpty() == true) {
        get(value)
    } else null

private fun initFlyway() {
    LOG.info("Executing Flyway")
    val numberOfMigrations = Flyway.configure()
        .dataSource(dataSource)
        .locations("db/migration")
        .load()
        .migrate().migrationsExecuted
    LOG.info("Applied $numberOfMigrations migrations")
}

private fun buildDataSource(dbHost: String, port: Int, database: String, username: String?, password: String?): DataSource {

    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = "jdbc:postgresql://$dbHost:$port/$database"
    if (username != null) {
        config.username = username
        config.password = password
    }
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}
