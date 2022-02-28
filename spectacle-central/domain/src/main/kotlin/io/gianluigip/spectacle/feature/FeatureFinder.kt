package io.gianluigip.spectacle.feature

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.feature.model.Feature

class FeatureFinder(
    private val featureRepo: FeatureRepository,
    private val transaction: TransactionExecutor
) {

    fun findAll(): List<Feature> = transaction.execute { featureRepo.findAll() }
}
