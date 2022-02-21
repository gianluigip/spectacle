package io.gianluigip.spectacle.specification.api.model

import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecificationStep
import kotlinx.serialization.Serializable

@Serializable
data class SpecificationsToUpdateRequest(
    val source: String,
    val component: String,
    val features: List<FeatureToUpdateRequest>,
)

@Serializable
data class FeatureToUpdateRequest(
    val name: String,
    val description: String,
    val specs: List<SpecificationToUpdateRequest>,
)

@Serializable
data class SpecificationToUpdateRequest(
    val team: String,
    val name: String,
    val status: SpecStatus,
    val tags: List<String> = listOf(),
    val steps: List<SpecificationStep>,
    val interactions: List<SpecInteraction> = emptyList(),
)
