package io.gianluigip.spectacle.feature.model

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import java.time.ZonedDateTime

data class Feature(
    val name: FeatureName,
    val description: String,
    val sources: List<Source>,
    val components: List<Component>,
    val creationTime: ZonedDateTime,
    val updateTime: ZonedDateTime,
)

data class FeatureToUpsert(
    val name: FeatureName,
    val description: String,
    val source: Source,
    val component: Component,
) {
    infix fun isNotEquals(spec: Feature) = !isEquals(spec)

    infix fun isEquals(spec: Feature) = name == spec.name && description == spec.description
}

data class FeatureToDelete(
    val name: FeatureName,
    val source: Source,
)
