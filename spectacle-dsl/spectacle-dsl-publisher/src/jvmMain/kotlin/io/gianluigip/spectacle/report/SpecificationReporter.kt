package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.report.config.ConfigLoader.CONFIG
import io.gianluigip.spectacle.report.publisher.Publishers
import io.gianluigip.spectacle.specification.Specification
import kotlinx.coroutines.runBlocking

actual object SpecificationReporter {

    private var isReportedInitialized = false

    @Synchronized
    actual fun initReport() {
        if (isReportedInitialized) return
        registerShutdownHook()
        Publishers.loadPublishers()
        isReportedInitialized = true
    }

    private fun registerShutdownHook() = Runtime.getRuntime().addShutdownHook(Thread { publishSpecs() })

    actual fun publishSpecs() = runBlocking {
        CONFIG.publishers.forEach {
            it.publishReport(ReportState.registeredSpecs(), CONFIG)
        }
    }

    actual fun registerSpec(spec: Specification) {
        ReportState.registerSpec(spec)
    }
}