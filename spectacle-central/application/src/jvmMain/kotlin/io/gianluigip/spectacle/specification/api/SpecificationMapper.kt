package io.gianluigip.spectacle.specification.api

import io.gianluigip.spectacle.specification.api.model.FeatureToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.FeatureToUpdate
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecificationToUpdate
import io.gianluigip.spectacle.specification.model.SpecificationsToUpdate
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

fun SpecificationsToUpdateRequest.toModel() =
    SpecificationsToUpdate(
        source = Source(source),
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
    )
