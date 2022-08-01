package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.CentralPublisherConfig

expect object CentralClientFactory {
    fun buildClient(config: CentralPublisherConfig): CentralClient
}
