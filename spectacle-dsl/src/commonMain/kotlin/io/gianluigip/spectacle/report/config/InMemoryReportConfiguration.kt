package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.report.publisher.SpecificationPublisher

data class InMemoryReportConfiguration(
    override val team: String,
    override val source: String,
    override val component: String,
    override val publishers: List<SpecificationPublisher>,
) : ReportConfiguration
