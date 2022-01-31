package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.report.publisher.SpecificationPublisher

data class ReportConfiguration(
    val team: String,
    val source: String,
    val component: String,
    val publishers: List<SpecificationPublisher>,
)
