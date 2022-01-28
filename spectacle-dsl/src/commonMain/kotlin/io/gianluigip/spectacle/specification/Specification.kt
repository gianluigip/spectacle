package io.gianluigip.spectacle.specification

data class Specification(
    val metadata: SpecificationMetadata,
    val name: String,
    val steps: List<SpecificationStep>,
)