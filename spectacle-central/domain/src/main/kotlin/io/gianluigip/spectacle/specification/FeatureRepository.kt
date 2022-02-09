package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.specification.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureToDelete
import io.gianluigip.spectacle.specification.model.FeatureToUpsert
import io.gianluigip.spectacle.specification.model.Source

interface FeatureRepository {

    fun findAll(): List<Feature>

    fun findBySource(source: Source): List<Feature>

    fun upsert(features: List<FeatureToUpsert>)

    fun delete(features: List<FeatureToDelete>)

}
