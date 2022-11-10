package io.gianluigip.spectacle.report.junit

import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.report.MetadataExtractor
import io.gianluigip.spectacle.report.SpecificationReporter
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class JUnitSpecificationReporter : AfterEachCallback {

    init {
        SpecificationReporter.initReport()
    }

    override fun afterEach(context: ExtensionContext) {
        val isTestFailed = context.executionException.isPresent
        if (isTestFailed) {
            TestContext.clearContext()
            return
        }

        val specMetadata = MetadataExtractor.extract(context.testMethod.get(), context.tags)
        val specBuilder = TestContext.getCurrentSpec()
        val specName = context.testMethod.get().name
        if (specMetadata == null || specBuilder == null) {
            TestContext.clearContext()
            return
        }

        val spec = specBuilder
            .fillMissingWithExternalMetadata(specName, specMetadata)
            .build()

        SpecificationReporter.registerSpec(spec)
    }

}
