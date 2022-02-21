package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.report.publisher.SpecificationPublisher
import io.gianluigip.spectacle.report.utils.removeStartAndEndSpacesOnEachLine
import io.gianluigip.spectacle.specification.Specification
import io.gianluigip.spectacle.specification.api.model.FeatureToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

object CentralPublisher : SpecificationPublisher {

    init {
        SpecificationPublisher.registerPublisher("central", this)
    }

    override suspend fun publishReport(specifications: List<Specification>, config: ReportConfiguration) {
        if (!config.centralEnabled) {
            println("Skipping Central publisher because it is disable.")
            return
        }
        val requestBody = generateRequestBody(specifications, config)
        postSpecs(requestBody, config)
    }

    private fun generateRequestBody(specifications: List<Specification>, config: ReportConfiguration): SpecificationsToUpdateRequest {
        val features = mutableListOf<FeatureToUpdateRequest>()
        val specificationsByFeature = specifications.groupBy { it.metadata.featureName }.entries.sortedBy { it.key }
        specificationsByFeature.forEach { (_, specsInFeature) ->
            val featureMetadata = specsInFeature.first().metadata
            val specs = mutableListOf<SpecificationToUpdateRequest>()
            specsInFeature.forEach { spec ->
                val specMetadata = spec.metadata
                val specToUpdate = SpecificationToUpdateRequest(
                    name = spec.name, team = specMetadata.team, status = spec.metadata.status, tags = specMetadata.tags,
                    steps = spec.steps, interactions = spec.interactions,
                )
                specs.add(specToUpdate)
            }
            val featureToUpdate = FeatureToUpdateRequest(
                name = featureMetadata.featureName,
                description = featureMetadata.featureDescription.removeStartAndEndSpacesOnEachLine(),
                specs = specs
            )
            features.add(featureToUpdate)
        }
        return SpecificationsToUpdateRequest(source = config.source, component = config.component, features = features)
    }

    private suspend fun postSpecs(requestBody: SpecificationsToUpdateRequest, config: ReportConfiguration) {

        val httpClient = HttpClient {
            install(ContentNegotiation) { json() }
        }

        val centralUrl = "${config.centralHost}api/specification"
        println("Publishing the specs to $centralUrl")

        try {
            val response = httpClient.put(centralUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            println("Publishing to Central finished with status ${response.status} and response ${response.bodyAsText()}")

        } catch (exception: Exception) {
            println("Central Publisher failed trying to communicate with the server: ${exception.message}")
            exception.printStackTrace()
        }
    }
}
