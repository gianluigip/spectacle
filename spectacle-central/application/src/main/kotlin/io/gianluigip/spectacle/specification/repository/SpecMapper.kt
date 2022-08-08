package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.common.utils.fromUtc
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.StepType
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.repository.tables.Specifications
import io.gianluigip.spectacle.specification.repository.tables.Tags
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import java.time.Clock
import io.gianluigip.spectacle.specification.repository.tables.SpecificationSteps as Steps
import io.gianluigip.spectacle.specification.repository.tables.SpecificationInteractions as Interactions

fun List<ResultRow>.toSpec(clock: Clock): Specification {
    val steps = distinctBy { it[Steps.id] }
        .filter { it[Steps.type] != null }
        .map {
            SpecificationStep(
                type = StepType.valueOf(it[Steps.type]),
                description = it[Steps.description],
                index = it[Steps.index],
            )
        }
    val tags = distinctBy { it[Tags.id] }.filter { it[Tags.name] != null }.map { TagName(it[Tags.name]) }.sortedBy { it.value }
    val interactions = distinctBy { it[Interactions.id] }.filter { it[Interactions.direction] != null }
        .map {
            SpecInteraction(
                direction = InteractionDirection.valueOf(it[Interactions.direction]),
                type = InteractionType.valueOf(it[Interactions.type]),
                name = it[Interactions.name],
                metadata = Json.decodeFromString(it[Interactions.metadata])
            )
        }
    return first().run { toSpec(clock, tags, steps, interactions) }
}

fun ResultRow.toSpec(
    clock: Clock,
    tags: List<TagName>,
    steps: List<SpecificationStep> = listOf(),
    interactions: List<SpecInteraction> = emptyList()
) =
    Specification(
        name = SpecName(get(Specifications.name)),
        feature = FeatureName(get(Specifications.feature)),
        team = TeamName(get(Specifications.team)),
        source = Source(get(Specifications.specSource)),
        component = Component(get(Specifications.component)),
        status = getOrNull(Specifications.status).toSpecStatus(),
        tags = tags,
        steps = steps,
        interactions = interactions,
        creationTime = get(Specifications.creationTime).fromUtc(clock),
        updateTime = get(Specifications.updateTime).fromUtc(clock),
    )

private fun String?.toSpecStatus(): SpecStatus =
    if (this == null || this.isBlank()) {
        SpecStatus.IMPLEMENTED
    } else {
        SpecStatus.valueOf(this)
    }
