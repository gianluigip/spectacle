package io.gianluigip.spectacle.specification

/**
 * Helper class to provide a mutable representation of a specification while the lib is still collecting the data.
 */
class SpecificationBuilder(
    var specName: String? = null,
    var featureName: String? = null,
    var featureDescription: String? = null,
    var team: String? = null,
    var status: SpecStatus? = null,
    var tags: MutableList<String> = mutableListOf(),
) {
    private val stepsRegistered: MutableList<SpecificationStep> = mutableListOf()
    val steps: List<SpecificationStep> get() = stepsRegistered

    internal fun addStep(type: StepType, description: String): SpecificationBuilder {
        stepsRegistered.add(SpecificationStep(type, description, index = steps.size + 1))
        return this
    }

    internal fun build() = Specification(
        metadata = SpecificationMetadata(
            featureName = featureName ?: "Unknown",
            featureDescription = featureDescription ?: "",
            team = team ?: "Unknown",
            status = status ?: SpecStatus.IMPLEMENTED,
            tags = tags.toList(),
        ),
        name = specName ?: "Unknown",
        steps = stepsRegistered.toList()
    )
}
