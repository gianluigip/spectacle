package io.gianluigip.spectacle.specification.api

import io.gianluigip.spectacle.common.utils.toKotlinInstant
import io.gianluigip.spectacle.specification.api.model.FeatureToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationResponse
import io.gianluigip.spectacle.specification.api.model.SpecificationToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.FeatureToUpdate
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.SpecificationToUpdate
import io.gianluigip.spectacle.specification.model.SpecificationsToUpdate
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

fun SpecificationsToUpdateRequest.toModel() =
    SpecificationsToUpdate(
        source = Source(source),
        component = Component(component),
        features = features.map { it.toModel() },
    )

fun FeatureToUpdateRequest.toModel() =
    FeatureToUpdate(
        name = FeatureName(name),
        description = description,
        specs = specs.map { it.toModel() }
    )

fun SpecificationToUpdateRequest.toModel() =
    SpecificationToUpdate(
        team = TeamName(team),
        name = name,
        status = status,
        tags = tags.map { TagName(it) },
        steps = steps,
        interactions = interactions,
    )

fun Specification.toResponse() = SpecificationResponse(
    name = name.value,
    feature = feature.value,
    team = team.value,
    source = source.value,
    component = component.value,
    status = status,
    tags = tags.map { it.value },
    steps = steps,
    interactions = interactions,
    creationTime = creationTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
)