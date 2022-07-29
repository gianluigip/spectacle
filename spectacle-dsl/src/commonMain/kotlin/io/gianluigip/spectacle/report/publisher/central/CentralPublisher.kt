package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.report.publisher.SpecificationPublisher
import io.gianluigip.spectacle.specification.Specification

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
