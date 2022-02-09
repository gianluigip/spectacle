package io.gianluigip.spectacle.report.publisher

import io.gianluigip.spectacle.report.publisher.central.CentralPublisher

object Publishers {

    internal fun loadPublishers() {
        TerminalPublisher
        CentralPublisher
    }
}