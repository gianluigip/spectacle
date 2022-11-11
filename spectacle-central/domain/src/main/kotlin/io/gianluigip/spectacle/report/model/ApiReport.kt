package io.gianluigip.spectacle.report.model

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

data class ApiReport(
    val components: List<ComponentApi>,
    val filters: ReportFilters,
)

data class ComponentApi(
    val component: Component,
    val endpoints: List<ComponentEndpoint>
)

data class ComponentEndpoint(
    val features: List<FeatureName>,
    val teams: List<TeamName>,
    val tags: List<TagName>,
    val sources: List<Source>,
    val path: String,
    val method: String,
    val queryParameters: Map<String, String>,
    val requests: List<EndpointRequest>,
)

data class EndpointRequest(
    val requestBody: String?,
    val requestContentType: String?,
    val responseBody: String,
    val responseStatus: String,
    val responseContentType: String?,
)
