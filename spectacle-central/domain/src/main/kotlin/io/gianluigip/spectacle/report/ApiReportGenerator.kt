package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.report.model.ApiReport
import io.gianluigip.spectacle.report.model.ComponentApi
import io.gianluigip.spectacle.report.model.ComponentEndpoint
import io.gianluigip.spectacle.report.model.EndpointRequest
import io.gianluigip.spectacle.report.model.ReportFilters
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.model.hasHttpMetadata
import io.gianluigip.spectacle.specification.model.toComponent
import java.util.SortedMap

private data class HttpInteraction(
    val component: Component, val feature: FeatureName, val team: TeamName, val tags: List<TagName>, val source: Source,
    val path: String, val method: String, val queryParameters: Map<String, String>, val requestBody: String?,
    val requestContentType: String?, val responseBody: String, val responseStatus: String, val responseContentType: String?,
)

class ApiReportGenerator(
    private val specFinder: SpecificationFinder,
    private val transaction: TransactionExecutor,
) {

    fun generateReport(
        pathToFilter: String? = null,
        features: Set<FeatureName>? = null,
        sources: Set<Source>? = null,
        components: Set<Component>? = null,
        tags: Set<TagName>? = null,
        teams: Set<TeamName>? = null,
    ): ApiReport = transaction.execute {

        val specs = specFinder.findBy(features = features, sources = sources, components = components, tags = tags, teams = teams)
        val httpInteractions = getHttpInteractions(specs, pathToFilter)

        ApiReport(
            components = generateComponentsApi(httpInteractions),
            filters = generateFilters(httpInteractions),
        )
    }

    private fun generateComponentsApi(httpInteractions: List<HttpInteraction>): List<ComponentApi> {
        val componentsApi = mutableListOf<ComponentApi>()
        httpInteractions.groupBy { it.component }.forEach { (component, componentEndpoints) ->

            val endpoints = mutableListOf<ComponentEndpoint>()
            componentEndpoints.groupBy { "${it.path}-${it.method}" }.forEach { (_, endpointRequests) ->

                val requests = endpointRequests.groupBy { it.responseStatus }.map { (_, requests) ->
                    requests.first().run {
                        EndpointRequest(
                            requestBody = requestBody,
                            requestContentType = requestContentType,
                            responseBody = responseBody,
                            responseStatus = responseStatus,
                            responseContentType = responseContentType
                        )
                    }
                }.sortedBy { it.responseStatus }

                val endpoint = ComponentEndpoint(
                    features = endpointRequests.map { it.feature }.distinct().sortedBy { it.value },
                    teams = endpointRequests.map { it.team }.distinct().sortedBy { it.value },
                    tags = endpointRequests.flatMap { it.tags }.distinct().sortedBy { it.value },
                    sources = endpointRequests.map { it.source }.distinct().sortedBy { it.value },
                    path = endpointRequests.first().path,
                    method = endpointRequests.first().method,
                    queryParameters = endpointRequests.mergeQueryParameters(),
                    requests = requests,
                )
                endpoints.add(endpoint)
            }
            componentsApi.add(
                ComponentApi(
                    component = component,
                    endpoints = endpoints.sortedBy { "${it.path}-${it.method}" },
                )
            )
        }
        return componentsApi.sortedBy { it.component.value }
    }

    private fun generateFilters(interactions: List<HttpInteraction>) = ReportFilters(
        features = interactions.map { it.feature }.toSet(),
        sources = interactions.map { it.source }.toSet(),
        components = interactions.map { it.component }.toSet(),
        tags = interactions.flatMap { it.tags }.toSet(),
        teams = interactions.map { it.team }.toSet(),
        statuses = emptySet(),
    )

    private fun getHttpInteractions(specs: List<Specification>, pathToFilter: String?): List<HttpInteraction> {
        val httpInteractions = mutableListOf<HttpInteraction>()
        specs.forEach { spec ->
            spec.interactions.forEach {
                if (it.isValidHttpInteraction(pathToFilter)) {
                    val metadata = it.toHttpMetadata()
                    httpInteractions.add(
                        HttpInteraction(
                            component = when (it.direction) {
                                INBOUND -> spec.component
                                OUTBOUND -> it.name.toComponent()
                            },
                            feature = spec.feature,
                            team = spec.team,
                            tags = spec.tags,
                            source = spec.source,
                            path = metadata.path,
                            method = metadata.method,
                            queryParameters = metadata.queryParameters,
                            requestBody = metadata.requestBody,
                            requestContentType = metadata.requestContentType,
                            responseBody = metadata.responseBody,
                            responseStatus = metadata.responseStatus,
                            responseContentType = metadata.responseContentType
                        )
                    )
                }
            }
        }
        return httpInteractions
    }

    private fun List<HttpInteraction>.mergeQueryParameters(): SortedMap<String, String> {
        val queryParameters = mutableMapOf<String, String>()
        forEach { interaction ->
            interaction.queryParameters.forEach { if (!queryParameters.contains(it.key)) queryParameters[it.key] = it.value }
        }
        return queryParameters.toSortedMap()
    }

    private fun SpecInteraction.isValidHttpInteraction(pathToFilter: String?) =
        hasHttpMetadata() && type == HTTP && (pathToFilter == null || metadata["path"]!!.uppercase().contains(pathToFilter.uppercase()))
}
