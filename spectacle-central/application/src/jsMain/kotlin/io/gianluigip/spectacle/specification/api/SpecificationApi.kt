package io.gianluigip.spectacle.specification.api

import io.gianluigip.spectacle.common.api.API_CLIENT
import io.gianluigip.spectacle.common.api.ENDPOINT
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun getSpecReport(
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
    status: SpecStatus? = null,
): SpecsReportResponse {
    return API_CLIENT.get("$ENDPOINT/api/report") {
        feature?.let { parameter("features", feature) }
        source?.let { parameter("sources", source) }
        component?.let { parameter("components", component) }
        tag?.let { parameter("tags", tag) }
        team?.let { parameter("teams", team) }
        status?.let { parameter("statuses", status) }
    }.body()
}
