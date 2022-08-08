package io.gianluigip.spectacle.feature.api.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class FeatureResponse(
    val name: String,
    val description: String,
    val sources: List<String>,
    val components: List<String>,
    val creationTime: Instant,
    val updateTime: Instant,
)
