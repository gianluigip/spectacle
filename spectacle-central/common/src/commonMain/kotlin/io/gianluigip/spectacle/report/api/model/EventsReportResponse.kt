package io.gianluigip.spectacle.report.api.model

import io.gianluigip.spectacle.specification.model.EventFormat
import kotlinx.serialization.Serializable

@Serializable
data class EventsReportResponse(
    val events: List<EventReportResponse>,
    val filters: ReportFiltersResponse,
)

@Serializable
data class EventReportResponse(
    val name: String,
    val producedBy: List<String>,
    val consumedBy: List<String>,
    val format: EventFormat?,
    val schema: String?,
    val dependencies: List<String>,
    val features: List<String>,
    val sources: List<String>,
    val components: List<String>,
    val tags: List<String>,
    val teams: List<String>,
)
