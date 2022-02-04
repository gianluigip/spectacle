package io.gianluigip.spectacle.specification.model

import kotlinx.serialization.Serializable

@Serializable
data class SpecificationStep(
    val type: StepType,
    val description: String,
    val index: Int,
) {
}

enum class StepType {
    GIVEN, AND_GIVEN, WHENEVER, AND_WHENEVER, THEN, AND_THEN, AND
}
