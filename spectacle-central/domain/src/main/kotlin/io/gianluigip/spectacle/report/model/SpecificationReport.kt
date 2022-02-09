package io.gianluigip.spectacle.report.model

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import java.time.ZonedDateTime

data class SpecsReport(
    val features: List<FeatureReport>,
    val filters: ReportFilters,
)

data class FeatureReport(
    val name: FeatureName,
//    val description:String TODO
    val specs: List<SpecReport>
)

data class SpecReport(
    val name: SpecName,
    val team: TeamName,
    val source: Source,
    val component: Component,
    val status: SpecStatus,
    val creationTime: ZonedDateTime,
    val updateTime: ZonedDateTime,
    val tags: List<TagName>,
    val steps: List<SpecificationStep>
)

data class ReportFilters(
    val features: Set<FeatureName>,
    val sources: Set<Source>,
    val components: Set<Component>,
    val tags: Set<TagName>,
    val teams: Set<TeamName>,
    val statuses: Set<SpecStatus>,
)
