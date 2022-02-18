package io.gianluigip.spectacle.diagram.api

import io.gianluigip.spectacle.common.api.API_CLIENT
import io.gianluigip.spectacle.common.api.ENDPOINT
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun getInteractionsReport(
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): SpecsReportResponse {
    return API_CLIENT.get("$ENDPOINT/api/report/interactions") {
        feature?.let { parameter("features", feature) }
        source?.let { parameter("sources", source) }
        component?.let { parameter("components", component) }
        tag?.let { parameter("tags", tag) }
        team?.let { parameter("teams", team) }
    }.body()
}
