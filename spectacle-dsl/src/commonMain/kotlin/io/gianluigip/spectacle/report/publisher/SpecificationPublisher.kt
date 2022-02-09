package io.gianluigip.spectacle.report.publisher

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.specification.Specification

private val publishers = mutableMapOf<String, SpecificationPublisher>()

interface SpecificationPublisher {

    companion object {
        fun registerPublisher(name: String, publisher: SpecificationPublisher) =
            publishers.put(name.uppercase(), publisher)

        fun findPublisher(name: String) = publishers[name.uppercase()]
    }

    suspend fun publishReport(specifications: List<Specification>, config: ReportConfiguration)
}
