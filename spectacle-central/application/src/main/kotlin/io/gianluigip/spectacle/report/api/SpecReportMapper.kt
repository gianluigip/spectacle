package io.gianluigip.spectacle.report.api

import io.gianluigip.spectacle.common.utils.toKotlinInstant
import io.gianluigip.spectacle.report.api.model.FeatureReportResponse
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.report.api.model.SpecReportResponse
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.report.model.FeatureReport
import io.gianluigip.spectacle.report.model.ReportFilters
import io.gianluigip.spectacle.report.model.SpecReport
import io.gianluigip.spectacle.report.model.SpecsReport

fun SpecsReport.toResponse() = SpecsReportResponse(
    features = features.map { it.toResponse() },
    filters = filters.toResponse(),
)

fun FeatureReport.toResponse() = FeatureReportResponse(
    name = name.value,
    description = description,
    specs = specs.map { it.toResponse() },
)

fun SpecReport.toResponse() = SpecReportResponse(
    name = name.value,
    team = team.value,
    source = source.value,
    component = component.value,
    status = status,
    tags = tags.map { it.value },
    steps = steps,
    creationTime = creationTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
)

fun ReportFilters.toResponse() = ReportFiltersResponse(
    features = features.asSequence().map { it.value }.toSet(),
    sources = sources.asSequence().map { it.value }.toSet(),
    components = components.asSequence().map { it.value }.toSet(),
    tags = tags.asSequence().map { it.value }.toSet(),
    teams = teams.asSequence().map { it.value }.toSet(),
    statuses = statuses,
)