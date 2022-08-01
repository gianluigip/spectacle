package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.CentralPublisherConfig

actual object CentralClientFactory {
    actual fun buildClient(config: CentralPublisherConfig): CentralClient = JvmCentralClient(config)
}
