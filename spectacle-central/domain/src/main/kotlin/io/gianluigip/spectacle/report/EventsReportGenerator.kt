package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.report.model.EventReport
import io.gianluigip.spectacle.report.model.EventsReport
import io.gianluigip.spectacle.report.model.ReportFilters
import io.gianluigip.spectacle.specification.SpecificationFinder
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.model.hasEventMetadata

class EventsReportGenerator(
    private val specFinder: SpecificationFinder,
    private val transaction: TransactionExecutor,
) {

    fun generateReport(
        eventName: String? = null,
        features: Set<FeatureName>? = null,
        sources: Set<Source>? = null,
        components: Set<Component>? = null,
        tags: Set<TagName>? = null,
        teams: Set<TeamName>? = null,
    ): EventsReport = transaction.execute {

        val specs = specFinder.findBy(
            interactionType = EVENT, interactionName = eventName, features = features, sources = sources,
            interactionComponents = components, tags = tags, teams = teams,
        )
        val events = generateEvents(specs)

        return@execute EventsReport(
            events = events,
            filters = generateFilters(events)
        )
    }

    private fun generateEvents(specs: List<Specification>): List<EventReport> {
        val eventsMap = mutableMapOf<String, MutableList<Pair<SpecInteraction, Specification>>>()
        specs.forEach { spec ->
            spec.interactions.forEach { interaction ->
                if (!eventsMap.containsKey(interaction.name)) eventsMap[interaction.name] = mutableListOf()
                eventsMap[interaction.name]!!.add(interaction to spec)
            }
        }

        return eventsMap.map { (eventName, interactionPairs) ->
            val metadata = interactionPairs.firstOrNull { it.first.hasEventMetadata() }?.first?.toEventMetadata()
            EventReport(
                name = eventName,
                producedBy = interactionPairs.filter { it.first.direction == OUTBOUND }.map { it.second.component }.toSet(),
                consumedBy = interactionPairs.filter { it.first.direction == INBOUND }.map { it.second.component }.toSet(),
                format = metadata?.format,
                schema = metadata?.schema,
                dependencies = metadata?.dependencies ?: emptyList(),
                features = interactionPairs.map { it.second.feature }.toSet(),
                sources = interactionPairs.map { it.second.source }.toSet(),
                components = interactionPairs.map { it.second.component }.toSet(),
                tags = interactionPairs.flatMap { it.second.tags }.toSet(),
                teams = interactionPairs.map { it.second.team }.toSet()
            )
        }
    }

    private fun generateFilters(events: List<EventReport>): ReportFilters =
        ReportFilters(
            features = events.flatMap { it.features }.toSet(),
            sources = events.flatMap { it.sources }.toSet(),
            components = events.flatMap { it.components }.toSet(),
            tags = events.flatMap { it.tags }.toSet(),
            teams = events.flatMap { it.teams }.toSet(),
            statuses = emptySet(),
        )
}
