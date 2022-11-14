package io.gianluigip.spectacle.common.fixtures

import io.gianluigip.spectacle.specification.Specification
import io.gianluigip.spectacle.specification.SpecificationMetadata
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.StepType

fun aSpecMetadata(
    featureName: String = "Feature1",
    featureDescription: String = "Description1",
    team: String = "Team1",
    status: SpecStatus = SpecStatus.IMPLEMENTED,
    tags: List<String> = listOf("Tag1"),
) = SpecificationMetadata(
    featureName = featureName, featureDescription = featureDescription, team = team, status = status, tags = tags
)

fun aSpecification(
    metadata: SpecificationMetadata = aSpecMetadata(),
    name: String = "Spec1",
    steps: List<SpecificationStep> = listOf(SpecificationStep(StepType.WHENEVER, "Step1", 0)),
    interactions: List<SpecInteraction> = listOf(
        SpecInteraction(InteractionDirection.INBOUND, InteractionType.EVENT, "Int1", mutableMapOf("m1" to "v1"))
    )

) = Specification(
    metadata = metadata, name = name, steps = steps, interactions = interactions,
)