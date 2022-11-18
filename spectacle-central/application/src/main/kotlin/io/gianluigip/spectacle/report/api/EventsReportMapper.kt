package io.gianluigip.spectacle.report.api

import io.gianluigip.spectacle.report.api.model.EventReportResponse
import io.gianluigip.spectacle.report.api.model.EventsReportResponse
import io.gianluigip.spectacle.report.model.EventReport
import io.gianluigip.spectacle.report.model.EventsReport

fun EventsReport.toResponse() = EventsReportResponse(
    events = events.sortedBy { it.name }.map { it.toResponse() },
    filters = filters.toResponse()
)

fun EventReport.toResponse() = EventReportResponse(
    name = name,
    producedBy = producedBy.map { it.value },
    consumedBy = consumedBy.map { it.value },
    format = format,
    schema = schema,
    dependencies = dependencies,
    features = features.map { it.value },
    sources = sources.map { it.value },
    components = components.map { it.value },
    tags = tags.map { it.value },
    teams = teams.map { it.value },
)
