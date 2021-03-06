package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.report.publisher.SpecificationPublisher
import io.ktor.http.Url

data class ReportConfiguration(
    val team: String,
    val source: String,
    val component: String,
    val publishers: List<SpecificationPublisher>,
    val centralConfig: CentralPublisherConfig,
)

data class CentralPublisherConfig(
    val enabled: Boolean,
    val host: Url?,
    val username: String,
    val password: String,
    val wikiEnabled: Boolean,
    val localWikiLocation: String?,
)
