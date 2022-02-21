package io.gianluigip.spectacle.report.api

import io.gianluigip.spectacle.report.api.model.InteractionsReportResponse
import io.gianluigip.spectacle.report.api.model.SystemInteractionResponse
import io.gianluigip.spectacle.report.model.InteractionsReport
import io.gianluigip.spectacle.report.model.SystemInteraction

fun InteractionsReport.toResponse() = InteractionsReportResponse(
    interactions = interactions.map { it.toResponse() },
    filters = filters.toResponse()
)

fun SystemInteraction.toResponse() = SystemInteractionResponse(
    component.value, interactionName, direction, type, metadata
)