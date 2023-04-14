package io.gianluigip.spectacle.report.publisher

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.specification.Specification

private val publishers = mutableMapOf<String, SpecificationPublisher>()

interface SpecificationPublisher {

    companion object {
        fun registerPublisher(name: String, publisher: SpecificationPublisher) =
            publishers.put(name.toUpperCase(), publisher)

        fun findPublisher(name: String) = publishers[name.toUpperCase()]
    }

    suspend fun publishReport(specifications: List<Specification>, config: ReportConfiguration)
}
