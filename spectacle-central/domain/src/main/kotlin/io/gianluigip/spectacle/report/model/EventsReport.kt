package io.gianluigip.spectacle.report.model

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.EventFormat
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

data class EventsReport(
    val events: List<EventReport>,
    val filters: ReportFilters,
)

data class EventReport(
    val name: String,
    val producedBy: Set<Component>,
    val consumedBy: Set<Component>,
    val format: EventFormat?,
    val schema: String?,
    val dependencies: List<String>,
    val features: Set<FeatureName>,
    val sources: Set<Source>,
    val components: Set<Component>,
    val tags: Set<TagName>,
    val teams: Set<TeamName>,
)
