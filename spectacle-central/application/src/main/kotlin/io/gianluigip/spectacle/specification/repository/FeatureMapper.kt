package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.common.utils.fromUtc
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.feature.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.repository.tables.Features.component
import io.gianluigip.spectacle.specification.repository.tables.Features.creationTime
import io.gianluigip.spectacle.specification.repository.tables.Features.description
import io.gianluigip.spectacle.specification.repository.tables.Features.featureSource
import io.gianluigip.spectacle.specification.repository.tables.Features.name
import io.gianluigip.spectacle.specification.repository.tables.Features.updateTime
import org.jetbrains.exposed.sql.ResultRow
import java.time.Clock

fun List<ResultRow>.toFeature(clock: Clock): Feature {
    val sources = asSequence().map { Source(it[featureSource]) }.sortedBy { it.value }.toList()
    val components = asSequence().map { Component(it[component]) }.distinct().sortedBy { it.value }.toList()
    val longestDescription = maxByOrNull { it[description].length }!![description]
    val minCreationTime = minByOrNull { it[creationTime] }!![creationTime].fromUtc(clock)
    val maxUpdateTime = maxByOrNull { it[updateTime] }!![updateTime].fromUtc(clock)
    return first().run {
        Feature(
            name = FeatureName(get(name)),
            description = longestDescription,
            creationTime = minCreationTime,
            updateTime = maxUpdateTime,
            sources = sources,
            components = components,
        )
    }
}
