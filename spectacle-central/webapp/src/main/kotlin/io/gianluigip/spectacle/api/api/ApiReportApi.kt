package io.gianluigip.spectacle.api.api

import io.gianluigip.spectacle.common.api.API_CLIENT
import io.gianluigip.spectacle.common.api.ENDPOINT
import io.gianluigip.spectacle.report.api.model.ApiReportResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun getApiReport(
    path: String? = null,
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): ApiReportResponse {
    return API_CLIENT.get("$ENDPOINT/api/report/api") {
        path?.let { parameter("path", path) }
        feature?.let { parameter("features", feature) }
        source?.let { parameter("sources", source) }
        component?.let { parameter("components", component) }
        tag?.let { parameter("tags", tag) }
        team?.let { parameter("teams", team) }
    }.body()
}
