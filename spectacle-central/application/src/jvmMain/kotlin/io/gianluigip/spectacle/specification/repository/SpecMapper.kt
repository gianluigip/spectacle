package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.StepType
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.repository.tables.Specifications
import io.gianluigip.spectacle.specification.repository.tables.Tags
import org.jetbrains.exposed.sql.ResultRow
import java.time.Clock
import io.gianluigip.spectacle.specification.repository.tables.SpecificationSteps as Steps

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
    return first().run { toSpec(clock, tags, steps) }
}

fun ResultRow.toSpec(clock: Clock, tags: List<TagName>, steps: List<SpecificationStep> = listOf()) = Specification(
    name = SpecName(get(Specifications.name)),
    feature = FeatureName(get(Specifications.feature)),
    team = TeamName(get(Specifications.team)),
    source = Source(get(Specifications.specSource)),
    status = getOrNull(Specifications.status).toSpecStatus(),
    tags = tags,
    steps = steps,
    creationTime = get(Specifications.creationTime).atZone(clock.zone),
    updateTime = get(Specifications.updateTime).atZone(clock.zone),
)

private fun String?.toSpecStatus(): SpecStatus =
    if (this == null || this.isBlank()) {
        SpecStatus.IMPLEMENTED
    } else {
        SpecStatus.valueOf(this)
    }
