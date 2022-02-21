package io.gianluigip.spectacle.common.beans

import io.gianluigip.spectacle.common.ExposedTransactionExecutor
import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.report.SpecReportGenerator
import io.gianluigip.spectacle.report.InteractionsReportGenerator
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.SpecificationProcessor
import io.gianluigip.spectacle.specification.repository.ExposedFeatureRepository
import io.gianluigip.spectacle.specification.repository.ExposedSpecificationRepository
import io.gianluigip.spectacle.specification.repository.ExposedTeamRepository
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import java.time.Clock

fun productionDependencies() = DI.Module("ProductionDependencies") {
    bindSingleton<Clock> { Clock.systemDefaultZone() }
    bindSingleton<TransactionExecutor> { ExposedTransactionExecutor() }

    bindSingleton { ExposedSpecificationRepository(instance()) }
    bindSingleton { ExposedFeatureRepository(instance()) }
    bindSingleton { ExposedTeamRepository(instance()) }

    bindSingleton { SpecificationProcessor(instance(), instance(), instance(), instance()) }
    bindSingleton { SpecificationFinder(instance(), instance()) }
    bindSingleton { SpecReportGenerator(instance(), instance(), instance()) }
    bindSingleton { InteractionsReportGenerator(instance(), instance()) }
}

var testDependencies = DI.Module("TestDependencies") {
}
