package io.gianluigip.spectacle.common.fixtures

import io.gianluigip.spectacle.specification.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import java.time.ZonedDateTime

object FeatureConstants {
    val FEATURE_NAME = FeatureName("Feature1")
    const val DESCRIPTION: String = "Short Description"
}

fun aFeature(
    name: FeatureName = FeatureConstants.FEATURE_NAME,
    description: String = FeatureConstants.DESCRIPTION,
    sources: List<Source> = FixtureConstants.SOURCES,
    creationTime: ZonedDateTime = FixtureConstants.CREATION_TIME,
    updateTime: ZonedDateTime = FixtureConstants.UPDATE_TIME,
) = Feature(name, description, sources, creationTime, updateTime)