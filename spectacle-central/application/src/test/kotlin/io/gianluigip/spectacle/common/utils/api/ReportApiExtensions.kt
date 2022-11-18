package io.gianluigip.spectacle.common.utils.api

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.dsl.interactions.receivesGetRequest
import io.gianluigip.spectacle.report.api.model.ApiReportResponse
import io.gianluigip.spectacle.report.api.model.EventsReportResponse
import io.gianluigip.spectacle.report.api.model.InteractionsReportResponse
import io.gianluigip.spectacle.report.api.model.SpecsReportResponse
import io.gianluigip.spectacle.report.model.EventsReport
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.ktor.client.call.body
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant

fun BaseIntegrationTest.getSpecReport(
    searchText: String? = null,
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
    status: SpecStatus? = null,
    updatedTimeAfter: Instant? = null,
): SpecsReportResponse = runBlocking {
    receivesGetRequest(
        path = "/api/report/specs",
        queryParameters = mapOf(
            "searchText" to searchText,
            "features" to feature,
            "sources" to source,
            "components" to component,
            "tags" to tag,
            "teams" to team,
            "statuses" to status?.name,
            "updatedTimeAfter" to updatedTimeAfter?.toString(),
        ).filter { it.value != null }.mapValues { it.value!! },
        fromComponent = "Web UI",
    ).body()
}

fun BaseIntegrationTest.getInteractionReport(
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): InteractionsReportResponse = runBlocking {
    receivesGetRequest(
        path = "/api/report/interactions",
        queryParameters = mapOf(
            "features" to feature,
            "sources" to source,
            "components" to component,
            "tags" to tag,
            "teams" to team,
        ).filter { it.value != null }.mapValues { it.value!! },
        fromComponent = "Web UI",
    ).body()
}

fun BaseIntegrationTest.getAPIReport(
    path: String? = null,
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): ApiReportResponse = runBlocking {
    receivesGetRequest(
        path = "/api/report/api",
        queryParameters = mapOf(
            "path" to path,
            "features" to feature,
            "sources" to source,
            "components" to component,
            "tags" to tag,
            "teams" to team,
        ).filter { it.value != null }.mapValues { it.value!! },
        fromComponent = "Web UI",
    ).body()
}

fun BaseIntegrationTest.getEventReport(
    eventName: String? = null,
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): EventsReportResponse = runBlocking {
    receivesGetRequest(
        path = "/api/report/events",
        queryParameters = mapOf(
            "eventName" to eventName,
            "features" to feature,
            "sources" to source,
            "components" to component,
            "tags" to tag,
            "teams" to team,
        ).filter { it.value != null }.mapValues { it.value!! },
        fromComponent = "Web UI",
    ).body()
}
