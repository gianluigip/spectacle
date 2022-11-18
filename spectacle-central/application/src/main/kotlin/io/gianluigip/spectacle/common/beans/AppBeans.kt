package io.gianluigip.spectacle.common.beans

import io.gianluigip.spectacle.auth.EnvVarsUserFinder
import io.gianluigip.spectacle.auth.UserFinder
import io.gianluigip.spectacle.auth.api.AuthProvider
import io.gianluigip.spectacle.common.ExposedTransactionExecutor
import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.feature.FeatureFinder
import io.gianluigip.spectacle.report.ApiReportGenerator
import io.gianluigip.spectacle.report.EventsReportGenerator
import io.gianluigip.spectacle.report.InteractionsReportGenerator
import io.gianluigip.spectacle.report.SpecReportGenerator
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.SpecificationProcessor
import io.gianluigip.spectacle.specification.repository.ExposedFeatureRepository
import io.gianluigip.spectacle.specification.repository.ExposedSpecificationRepository
import io.gianluigip.spectacle.specification.repository.ExposedTeamRepository
import io.gianluigip.spectacle.team.TeamFinder
import io.gianluigip.spectacle.wiki.WikiFinder
import io.gianluigip.spectacle.wiki.WikiProcessor
import io.gianluigip.spectacle.wiki.repository.ExposedWikiPageRepository
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import java.time.Clock

fun productionDependencies() = DI.Module("ProductionDependencies") {
    bindSingleton<Clock> { Clock.systemDefaultZone() }
    bindSingleton<TransactionExecutor> { ExposedTransactionExecutor() }
    bindSingleton<UserFinder> { EnvVarsUserFinder() }
    bindSingleton { AuthProvider(instance()) }

    bindSingleton { ExposedSpecificationRepository(instance()) }
    bindSingleton { ExposedFeatureRepository(instance()) }
    bindSingleton { ExposedTeamRepository(instance()) }
    bindSingleton { ExposedWikiPageRepository(instance()) }

    bindSingleton { SpecificationProcessor(instance(), instance(), instance(), instance()) }
    bindSingleton { SpecificationFinder(instance(), instance()) }
    bindSingleton { SpecReportGenerator(instance(), instance(), instance()) }
    bindSingleton { InteractionsReportGenerator(instance(), instance()) }
    bindSingleton { ApiReportGenerator(instance(), instance()) }
    bindSingleton { EventsReportGenerator(instance(), instance()) }
    bindSingleton { WikiProcessor(instance(), instance()) }
    bindSingleton { WikiFinder(instance(), instance()) }
    bindSingleton { FeatureFinder(instance(), instance()) }
    bindSingleton { TeamFinder(instance(), instance()) }
}

var testDependencies = DI.Module("TestDependencies") {
}
