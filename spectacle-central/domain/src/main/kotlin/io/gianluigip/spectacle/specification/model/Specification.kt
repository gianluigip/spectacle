package io.gianluigip.spectacle.specification.model

import java.time.ZonedDateTime

data class Specification(
    val name: SpecName,
    val feature: FeatureName,
    val team: TeamName,
    val source: Source,
    val component: Component,
    val status: SpecStatus,
    val tags: List<TagName>,
    val steps: List<SpecificationStep>,
    val interactions: List<SpecInteraction>,
    val creationTime: ZonedDateTime = ZonedDateTime.now(),
    val updateTime: ZonedDateTime = ZonedDateTime.now(),
)
