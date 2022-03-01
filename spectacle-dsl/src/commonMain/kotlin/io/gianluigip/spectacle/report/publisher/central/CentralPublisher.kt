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
        if (!config.centralConfig.enabled) {
            println("Skipping Central publisher because it is disable.")
            return
        }
        val centralClient = CentralClient(config.centralConfig)
        CentralSpecPublisher.publishSpecs(specifications, centralClient, config)
        CentralWikiPublisher.publishWiki(centralClient, config)
    }
}
