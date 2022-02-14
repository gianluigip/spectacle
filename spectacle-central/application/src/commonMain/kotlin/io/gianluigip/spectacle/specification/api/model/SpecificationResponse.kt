package io.gianluigip.spectacle.specification.api.model

import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecificationStep
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class SpecificationResponse(
    val name: String,
    val feature: String,
    val team: String,
    val source: String,
    val component: String,
    val status: SpecStatus,
    val tags: List<String>,
    val steps: List<SpecificationStep>,
    val creationTime: Instant,
    val updateTime: Instant,
)
