package io.gianluigip.spectacle.common.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import javax.sql.DataSource

private val LOG = LoggerFactory.getLogger("DbConfig")

private fun datasource(): DataSource {

    val dbUrl = System.getenv("DATABASE_URL").replace("postgres://", "")
    val hostUrl = dbUrl.substring(dbUrl.indexOf("@") + 1)
    val dbUsername = dbUrl.substring(0, dbUrl.indexOf(":"))
    val dbPassword = dbUrl.substring(dbUrl.indexOf(":") + 1, dbUrl.indexOf("@"))
    val jdbcUrl = "jdbc:postgresql://$hostUrl"

    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = jdbcUrl
    config.username = dbUsername
    config.password = dbPassword
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}

fun initFlyway() {
    LOG.info("Executing Flyway")
    val numberOfMigrations = Flyway.configure()
        .dataSource(datasource())
        .locations("db/migration")
        .load()
        .migrate().migrationsExecuted
    LOG.info("Applied $numberOfMigrations migrations")
}
