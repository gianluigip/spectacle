package io.gianluigip.spectacle.feature.api

import io.gianluigip.spectacle.common.utils.toKotlinInstant
import io.gianluigip.spectacle.feature.api.model.FeatureResponse
import io.gianluigip.spectacle.feature.model.Feature

fun Feature.toResponse() = FeatureResponse(
    name = name.value,
    description = description,
    sources = sources.map { it.value }.sorted(),
    components = components.map { it.value }.sorted(),
    creationTime = creationTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
)
