package io.gianluigip.spectacle.common.utils.db

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.feature.model.Feature
import org.jetbrains.exposed.sql.transactions.transaction

fun BaseIntegrationTest.findAllFeatures(): List<Feature> = transaction {
    usesPersistence()
    featureRepo.findAll()
}