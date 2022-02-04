package io.gianluigip.spectacle.specification.model

data class SpecificationsToUpdate(
    val source: Source,
    val features: List<FeatureToUpdate>,
)

data class FeatureToUpdate(
    val name: FeatureName,
    val description: String,
    val specs: List<SpecificationToUpdate>,
)

data class SpecificationToUpdate(
    val team: TeamName,
    val name: String,
    val status: SpecStatus,
    val tags: List<TagName> = listOf(),
    val steps: List<SpecificationStep>
)
