package io.gianluigip.spectacle.report.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiReportResponse(
    val components: List<ComponentApiResponse>,
    val filters: ReportFiltersResponse,
)

@Serializable
data class ComponentApiResponse(
    val component: String,
    val endpoints: List<ComponentEndpointResponse>
)

@Serializable
data class ComponentEndpointResponse(
    val features: List<String>,
    val teams: List<String>,
    val tags: List<String>,
    val sources: List<String>,
    val path: String,
    val method: String,
    val queryParameters: Map<String, String>,
    val requests: List<EndpointRequestResponse>,
)

@Serializable
data class EndpointRequestResponse(
    val requestBody: String?,
    val requestContentType: String?,
    val responseBody: String,
    val responseStatus: String,
    val responseContentType: String?,
)
