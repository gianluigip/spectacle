package io.gianluigip.spectacle.common.utils.db

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.specification.model.Specification
import org.jetbrains.exposed.sql.transactions.transaction

fun BaseIntegrationTest.findAllSpecs(): List<Specification> = transaction {
    specRepo.findAll()
}
