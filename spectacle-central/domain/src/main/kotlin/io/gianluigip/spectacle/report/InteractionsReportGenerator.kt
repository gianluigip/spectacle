package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.report.model.InteractionsReport
import io.gianluigip.spectacle.report.model.ReportFilters
import io.gianluigip.spectacle.report.model.SystemInteraction
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

class InteractionsReportGenerator(
    private val specFinder: SpecificationFinder,
    private val transaction: TransactionExecutor,
) {

    fun generateReport(
        searchText: String? = null,
        features: Set<FeatureName>? = null,
        sources: Set<Source>? = null,
        components: Set<Component>? = null,
        tags: Set<TagName>? = null,
        teams: Set<TeamName>? = null,
    ): InteractionsReport = transaction.execute {

        val specs = specFinder.findBy(searchText, features, sources, components, tags, teams)
        val interactions = mutableMapOf<String, SystemInteraction>()
        specs.forEach { spec ->
            spec.interactions.forEach { interaction ->

                val key = "${spec.component}-${interaction.name}-${interaction.direction}-${interaction.type}"
                val systemInteraction = if (interactions.containsKey(key)) {
                    SystemInteraction(
                        component = spec.component,
                        interactionName = interaction.name,
                        direction = interaction.direction,
                        type = interaction.type,
                        metadata = mutableMapOf<String, String>().apply {
                            putAll(interactions[key]!!.metadata)
                            putAll(interaction.metadata)
                        }
                    )
                } else SystemInteraction(
                    component = spec.component,
                    interactionName = interaction.name,
                    direction = interaction.direction,
                    type = interaction.type,
                    metadata = interaction.metadata
                )
                interactions[key] = systemInteraction
            }
        }
        return@execute InteractionsReport(interactions.values.toSet(), generateFilters(specs))
    }

    private fun generateFilters(specs: List<Specification>): ReportFilters =
        ReportFilters(
            features = specs.map { it.feature }.toSet(),
            sources = specs.map { it.source }.toSet(),
            components = specs.map { it.component }.toSet(),
            tags = specs.flatMap { it.tags }.toSet(),
            teams = specs.map { it.team }.toSet(),
            statuses = specs.map { it.status }.toSet(),
        )
}