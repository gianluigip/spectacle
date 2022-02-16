package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.report.model.FeatureReport
import io.gianluigip.spectacle.report.model.ReportFilters
import io.gianluigip.spectacle.report.model.SpecReport
import io.gianluigip.spectacle.report.model.SpecsReport
import io.gianluigip.spectacle.specification.FeatureRepository
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

class ReportGenerator(
    private val specFinder: SpecificationFinder,
    private val featureRepo: FeatureRepository,
    private val transaction: TransactionExecutor,
) {

    fun generateReport(
        features: Set<FeatureName>? = null,
        sources: Set<Source>? = null,
        components: Set<Component>? = null,
        tags: Set<TagName>? = null,
        teams: Set<TeamName>? = null,
        statuses: Set<SpecStatus>? = null,
    ): SpecsReport = transaction.execute {

        val specs = specFinder.findBy(features, sources, components, tags, teams, statuses)
            .sortedWith(compareBy({ it.feature.value }, { it.name.value }))
        val features = findFeatures(specs)

        val featuresReport = mutableListOf<FeatureReport>()
        specs.groupBy { it.feature }.forEach { (feature, featureSpecs) ->
            val specsReport = mutableListOf<SpecReport>()
            featureSpecs.forEach { it ->
                val specReport = SpecReport(
                    name = it.name, team = it.team, source = it.source, component = it.component, steps = it.steps.sortedBy { it.index },
                    tags = it.tags, status = it.status, creationTime = it.creationTime, updateTime = it.updateTime
                )
                specsReport.add(specReport)
            }
            featuresReport.add(FeatureReport(name = feature, description = features[feature]?.description ?: "", specs = specsReport))
        }
        return@execute SpecsReport(features = featuresReport, generateFilters(featuresReport))
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

    private fun findFeatures(specs: List<Specification>): Map<FeatureName, Feature> {
        val featureNames = specs.asSequence().map { it.feature }.toSet()
        return featureRepo.findByNames(featureNames).associateBy { it.name }
    }
}
