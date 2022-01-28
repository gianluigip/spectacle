package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.report.publisher.SpecificationPublisher

interface ReportConfiguration {
    val team: String
    val source: String
    val component: String
    val publishers: List<SpecificationPublisher>
}
