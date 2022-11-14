package io.gianluigip.spectacle.report.api

import io.gianluigip.spectacle.report.api.model.ApiReportResponse
import io.gianluigip.spectacle.report.api.model.ComponentApiResponse
import io.gianluigip.spectacle.report.api.model.ComponentEndpointResponse
import io.gianluigip.spectacle.report.api.model.EndpointRequestResponse
import io.gianluigip.spectacle.report.model.ApiReport
import io.gianluigip.spectacle.report.model.ComponentApi
import io.gianluigip.spectacle.report.model.ComponentEndpoint
import io.gianluigip.spectacle.report.model.EndpointRequest

fun ApiReport.toResponse() = ApiReportResponse(
    components = components.map { it.toResponse() },
    filters = filters.toResponse(),
)

fun ComponentApi.toResponse() = ComponentApiResponse(
    component = component.value,
    endpoints = endpoints.map { it.toResponse() }
)

fun ComponentEndpoint.toResponse() = ComponentEndpointResponse(
    features = features.map { it.value },
    teams = teams.map { it.value },
    tags = tags.map { it.value },
    sources = sources.map { it.value },
    path = path,
    method = method,
    queryParameters = queryParameters,
    requests = requests.map { it.toResponse() }
)

fun EndpointRequest.toResponse() = EndpointRequestResponse(
    requestBody = requestBody,
    requestContentType = requestContentType,
    responseBody = responseBody,
    responseStatus = responseStatus,
    responseContentType = responseContentType
)
