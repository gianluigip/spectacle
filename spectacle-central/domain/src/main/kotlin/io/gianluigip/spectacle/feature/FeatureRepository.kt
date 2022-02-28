package io.gianluigip.spectacle.feature

import io.gianluigip.spectacle.feature.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.feature.model.FeatureToDelete
import io.gianluigip.spectacle.feature.model.FeatureToUpsert
import io.gianluigip.spectacle.specification.model.Source

interface FeatureRepository {

    fun findAll(): List<Feature>

    fun findBySource(source: Source): List<Feature>

    fun findByNames(names: Collection<FeatureName>): List<Feature>

    fun upsert(features: List<FeatureToUpsert>)

    fun delete(features: List<FeatureToDelete>)

}
