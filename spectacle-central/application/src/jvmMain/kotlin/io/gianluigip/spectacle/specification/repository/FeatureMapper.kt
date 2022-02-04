package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.specification.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.repository.tables.Features.creationTime
import io.gianluigip.spectacle.specification.repository.tables.Features.description
import io.gianluigip.spectacle.specification.repository.tables.Features.featureSource
import io.gianluigip.spectacle.specification.repository.tables.Features.name
import io.gianluigip.spectacle.specification.repository.tables.Features.updateTime
import org.jetbrains.exposed.sql.ResultRow
import java.time.Clock

fun List<ResultRow>.toFeature(clock: Clock): Feature {
    val sources = map { Source(it[featureSource]) }
    val longestDescription = maxByOrNull { it[description].length }!![description]
    val minCreationTime = minByOrNull { it[creationTime] }!![creationTime].atZone(clock.zone)
    val maxUpdateTime = maxByOrNull { it[updateTime] }!![updateTime].atZone(clock.zone)
    return first().run {
        Feature(
            name = FeatureName(get(name)),
            description = longestDescription,
            creationTime = minCreationTime,
            updateTime = maxUpdateTime,
            sources = sources,
        )
    }
}
