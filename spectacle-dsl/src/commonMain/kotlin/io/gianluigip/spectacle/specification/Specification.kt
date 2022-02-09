package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecificationStep

data class Specification(
    val metadata: SpecificationMetadata,
    val name: String,
    val steps: List<SpecificationStep>,
)

data class SpecificationMetadata(
    val featureName: String,
    val featureDescription: String,
    val team: String,
    val status: SpecStatus,
    val tags: List<String>,
)
