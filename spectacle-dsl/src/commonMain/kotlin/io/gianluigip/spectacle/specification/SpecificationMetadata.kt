package io.gianluigip.spectacle.specification

data class SpecificationMetadata(
    val featureName: String,
    val featureDescription: String,
    val team: String,
    val status: SpecStatus,
    val tags: List<String>,
)

enum class SpecStatus {
    IMPLEMENTED, PARTIALLY_IMPLEMENTED, NOT_IMPLEMENTED
}
