package io.gianluigip.spectacle.common.beans

import io.gianluigip.spectacle.common.ExposedTransactionExecutor
import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.report.ReportGenerator
import io.gianluigip.spectacle.specification.FeatureRepository
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.SpecificationProcessor
import io.gianluigip.spectacle.specification.SpecificationRepository
import io.gianluigip.spectacle.specification.TeamRepository
import io.gianluigip.spectacle.specification.repository.ExposedFeatureRepository
import io.gianluigip.spectacle.specification.repository.ExposedSpecificationRepository
import io.gianluigip.spectacle.specification.repository.ExposedTeamRepository
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import java.time.Clock

fun DI.MainBuilder.registerAllBeans() {
    bindSingleton { Clock.systemDefaultZone() }
    bindSingleton<TransactionExecutor> { ExposedTransactionExecutor() }

    bindSingleton<SpecificationRepository> { ExposedSpecificationRepository(instance()) }
    bindSingleton<FeatureRepository> { ExposedFeatureRepository(instance()) }
    bindSingleton<TeamRepository> { ExposedTeamRepository(instance()) }

    bindSingleton { SpecificationProcessor(instance(), instance(), instance(), instance()) }
    bindSingleton { SpecificationFinder(instance(), instance()) }
    bindSingleton { ReportGenerator(instance(), instance()) }
}