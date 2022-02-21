package io.gianluigip.spectacle.report.model

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType

data class InteractionsReport(
    val interactions: Set<SystemInteraction>,
    val filters: ReportFilters,
)

data class SystemInteraction(
    val component: Component,
    val interactionName: String,
    val direction: InteractionDirection,
    val type: InteractionType,
    val metadata: Map<String, String>,
)
