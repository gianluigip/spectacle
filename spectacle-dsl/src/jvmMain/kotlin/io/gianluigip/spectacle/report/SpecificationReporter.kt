package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.report.config.FileReportConfiguration
import io.gianluigip.spectacle.report.publisher.TerminalPublisher
import io.gianluigip.spectacle.specification.Specification

actual object SpecificationReporter {

    private var isReportedInitialized = false

    @Synchronized
    actual fun initReport() {
        if (isReportedInitialized) return
        registerShutdownHook()
        loadPublishers()
        isReportedInitialized = true
    }

    private fun registerShutdownHook() = Runtime.getRuntime().addShutdownHook(Thread { publishSpecs() })

    private fun loadPublishers() {
        TerminalPublisher
    }

    actual fun publishSpecs() {
        FileReportConfiguration.publishers.forEach {
            it.publishReport(ReportState.registeredSpecs(), FileReportConfiguration)
        }
    }

    actual fun registerSpec(spec: Specification) {
        ReportState.registerSpec(spec)
    }
}