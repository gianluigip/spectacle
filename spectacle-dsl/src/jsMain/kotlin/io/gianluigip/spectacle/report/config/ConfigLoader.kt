package io.gianluigip.spectacle.report.config

import io.gianluigip.spectacle.report.publisher.TerminalPublisher

actual object ConfigLoader {

    actual val CONFIG: ReportConfiguration
        get() {
            println("Warning only default config is supported for JavaScript.")
            return ReportConfiguration(
                team = "Other",
                source = "Other",
                component = "Other",
                publishers = listOf(TerminalPublisher),
                centralConfig = CentralPublisherConfig(
                    enabled = false,
                    host = null,
                    username = "",
                    password = "",
                    publishEmptySpecs = false,
                    wikiEnabled = false,
                    localWikiLocation = null,
                ),
            )
        }

}