package io.gianluigip.spectacle.specification.model

data class SpecToUpsert(
    val name: SpecName,
    val feature: FeatureName,
    val team: TeamName,
    val source: Source,
    val status: SpecStatus,
    val tags: List<TagName>,
    val steps: List<SpecificationStep>,
) {
    infix fun isNotEquals(spec: Specification) = !isEquals(spec)

    infix fun isEquals(spec: Specification) =
        name == spec.name && feature == spec.feature && team == spec.team && source == spec.source
                && status == spec.status && steps == spec.steps
}
