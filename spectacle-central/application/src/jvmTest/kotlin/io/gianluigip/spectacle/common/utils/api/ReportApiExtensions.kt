package io.gianluigip.spectacle.common.utils.api

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.report.api.model.InteractionsReportResponse
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.runBlocking

fun BaseIntegrationTest.getSpecReport(
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
    status: SpecStatus? = null,
): SpecsReportResponse = runBlocking {
    httpClient.get("$httpHost/api/report/specs") {
        feature?.let { parameter("features", feature) }
        source?.let { parameter("sources", source) }
        component?.let { parameter("components", component) }
        tag?.let { parameter("tags", tag) }
        team?.let { parameter("teams", team) }
        status?.let { parameter("statuses", status) }
    }.body()
}

fun BaseIntegrationTest.getInteractionReport(
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): InteractionsReportResponse = runBlocking {
    httpClient.get("$httpHost/api/report/interactions") {
        feature?.let { parameter("features", feature) }
        source?.let { parameter("sources", source) }
        component?.let { parameter("components", component) }
        tag?.let { parameter("tags", tag) }
        team?.let { parameter("teams", team) }
    }.body()
}