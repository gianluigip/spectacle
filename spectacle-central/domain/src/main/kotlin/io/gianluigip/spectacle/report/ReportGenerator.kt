package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.report.model.FeatureReport
import io.gianluigip.spectacle.report.model.ReportFilters
import io.gianluigip.spectacle.report.model.SpecReport
import io.gianluigip.spectacle.report.model.SpecsReport
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

class ReportGenerator(
    private val specFinder: SpecificationFinder,
    private val transaction: TransactionExecutor,
) {

    fun generateReport(
        feature: FeatureName? = null,
        source: Source? = null,
        component: Component? = null,
        tag: TagName? = null,
        team: TeamName? = null,
        status: SpecStatus? = null,
    ): SpecsReport = transaction.execute {

        val specs = specFinder.findBy(feature, source, component, tag, team, status)
            .sortedWith(compareBy({ it.feature.value }, { it.name.value }))

        val features = mutableListOf<FeatureReport>()
        specs.groupBy { it.feature }.forEach { (feature, featureSpecs) ->
            val specsReport = mutableListOf<SpecReport>()
            featureSpecs.forEach { it ->
                val specReport = SpecReport(
                    name = it.name, team = it.team, source = it.source, component = it.component, steps = it.steps.sortedBy { it.index },
                    tags = it.tags, status = it.status, creationTime = it.creationTime, updateTime = it.updateTime
                )
                specsReport.add(specReport)
            }
            features.add(FeatureReport(name = feature, specs = specsReport))
        }
        return@execute SpecsReport(features = features, generateFilters(features))
    }

    private fun generateFilters(features: List<FeatureReport>): ReportFilters =
        ReportFilters(
            features = features.map { it.name }.toSet(),
            sources = features.asSequence().flatMap { it.specs }.map { it.source }.toSet(),
            components = features.asSequence().flatMap { it.specs }.map { it.component }.toSet(),
            tags = features.asSequence().flatMap { it.specs }.flatMap { it.tags }.toSet(),
            teams = features.asSequence().flatMap { it.specs }.map { it.team }.toSet(),
            statuses = features.asSequence().flatMap { it.specs }.map { it.status }.toSet(),
        )
}
