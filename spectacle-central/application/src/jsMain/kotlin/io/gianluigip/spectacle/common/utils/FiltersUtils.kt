package io.gianluigip.spectacle.common.utils

import io.gianluigip.spectacle.specification.component.FiltersSelected

fun buildReportUrlWithParameters(reportPath: String, filters: FiltersSelected): String {
    val params = mutableListOf<String>()
    if (filters.feature != null) params.add("feature=${filters.feature.escapeSpaces()}")
    if (filters.tag != null) params.add("tag=${filters.tag.escapeSpaces()}")
    if (filters.source != null) params.add("source=${filters.source.escapeSpaces()}")
    if (filters.component != null) params.add("component=${filters.component.escapeSpaces()}")
    if (filters.team != null) params.add("team=${filters.team.escapeSpaces()}")
    if (filters.status != null) params.add("status=${filters.status.name}")

    val searchSegment = if (params.isNotEmpty()) {
        "?${params.joinToString("&")}"
    } else ""
    return "$reportPath$searchSegment"
}
