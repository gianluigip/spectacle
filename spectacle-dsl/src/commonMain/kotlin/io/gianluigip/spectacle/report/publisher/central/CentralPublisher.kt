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
            return println("Skipping Central publisher because it is disable.")
        }
        if (specifications.isEmpty() && !config.centralConfig.publishEmptySpecs) {
            return println(
                "Skipping Central publisher because there wasn't any spec to publish, " +
                        "you can force to publish using the property 'specification.publisher.central.publish-empty-specs'."
            )
        }
        val centralClient = CentralClientFactory.buildClient(config.centralConfig)
        CentralSpecPublisher.publishSpecs(specifications, centralClient, config)
        CentralWikiPublisher.publishWiki(centralClient, config)
    }
}
