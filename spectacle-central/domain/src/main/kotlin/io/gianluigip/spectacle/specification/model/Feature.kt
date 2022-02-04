package io.gianluigip.spectacle.specification.model

import java.time.ZonedDateTime

@JvmInline
value class FeatureName(val value: String)

fun String.toFeature() = FeatureName(this)

data class Feature(
    val name: FeatureName,
    val description: String,
    val sources: List<Source>,
    val creationTime: ZonedDateTime,
    val updateTime: ZonedDateTime,
)

data class FeatureToUpsert(
    val name: FeatureName,
    val description: String,
    val source: Source,
) {
    infix fun isNotEquals(spec: Feature) = !isEquals(spec)

    infix fun isEquals(spec: Feature) = name == spec.name && description == spec.description
}

data class FeatureToDelete(
    val name: FeatureName,
    val source: Source,
)
