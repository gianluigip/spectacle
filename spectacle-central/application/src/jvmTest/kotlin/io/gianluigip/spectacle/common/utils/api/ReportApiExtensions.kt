package io.gianluigip.spectacle.common.utils.api

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.runBlocking

fun BaseIntegrationTest.getReport(
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
    status: SpecStatus? = null,
): SpecsReportResponse = runBlocking {
    httpClient.get("$httpHost/api/report") {
        feature?.let { parameter("feature", feature) }
        source?.let { parameter("source", source) }
        component?.let { parameter("component", component) }
        tag?.let { parameter("tag", tag) }
        team?.let { parameter("team", team) }
        status?.let { parameter("status", status) }
    }.body()
}