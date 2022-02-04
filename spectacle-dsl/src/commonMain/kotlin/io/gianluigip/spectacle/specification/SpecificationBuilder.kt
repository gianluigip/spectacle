package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.StepType

/**
 * Helper class to provide a mutable representation of a specification while the lib is still collecting the data.
 */
class SpecificationBuilder(
    var specName: String? = null,
    var featureName: String? = null,
    var featureDescription: String? = null,
    var team: String? = null,
    var status: SpecStatus? = null,
    var tags: MutableSet<String> = mutableSetOf(),
) {
    private val stepsRegistered: MutableList<SpecificationStep> = mutableListOf()
    val steps: List<SpecificationStep> get() = stepsRegistered

    internal fun addStep(type: StepType, description: String): SpecificationBuilder {
        stepsRegistered.add(SpecificationStep(type, description, index = steps.size + 1))
        return this
    }

    internal fun fillMissingWithExternalMetadata(externalSpecName: String, metadata: SpecificationMetadata): SpecificationBuilder {
        specName = specName ?: externalSpecName
        featureName = featureName ?: metadata.featureName
        featureDescription = featureDescription ?: metadata.featureDescription
        team = team ?: metadata.team
        status = status ?: metadata.status
        tags.addAll(metadata.tags)
        return this
    }

    internal fun build() = Specification(
        metadata = SpecificationMetadata(
            featureName = featureName ?: "Unknown",
            featureDescription = featureDescription ?: "",
            team = team ?: "Unknown",
            status = status ?: SpecStatus.IMPLEMENTED,
            tags = tags.toList().sortedBy { it },
        ),
        name = specName ?: "Unknown",
        steps = stepsRegistered.toList()
    )
}
