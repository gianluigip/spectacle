package io.gianluigip.spectacle.common.utils.db

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.specification.model.Team
import org.jetbrains.exposed.sql.transactions.transaction

fun BaseIntegrationTest.findAllTeams(): List<Team> = transaction {
    usesPersistence()
    teamRepo.findAll()
}