package io.gianluigip.spectacle.report.api.model

import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import kotlinx.serialization.Serializable

@Serializable
data class InteractionsReportResponse(
    val interactions: List<SystemInteractionResponse>,
    val filters: ReportFiltersResponse,
)

@Serializable
data class SystemInteractionResponse(
    val component: String,
    val interactionName: String,
    val direction: InteractionDirection,
    val type: InteractionType,
    val metadata: Map<String, String>,
)
