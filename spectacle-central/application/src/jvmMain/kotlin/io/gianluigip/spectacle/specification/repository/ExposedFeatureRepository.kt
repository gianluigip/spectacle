package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.common.utils.toUtcLocalDateTime
import io.gianluigip.spectacle.specification.FeatureRepository
import io.gianluigip.spectacle.specification.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureToDelete
import io.gianluigip.spectacle.specification.model.FeatureToUpsert
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.repository.tables.Features
import io.gianluigip.spectacle.specification.repository.tables.Features.featureSource
import io.gianluigip.spectacle.specification.repository.tables.Features.id
import io.gianluigip.spectacle.specification.repository.tables.Features.name
import io.gianluigip.spectacle.specification.repository.tables.searchExpression
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.compoundOr
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.Clock
import java.util.UUID

class ExposedFeatureRepository(
    private val clock: Clock,
) : FeatureRepository {

    fun deleteAll() {
        Features.deleteAll()
    }

    override fun findAll(): List<Feature> = Features.selectAll().toFeatures()

    override fun findBySource(source: Source): List<Feature> =
        Features.select { featureSource eq source.value }.toFeatures()

    private fun Query.toFeatures() = orderBy(name to SortOrder.ASC)
        .groupBy { it[name] }.values.map { it.sortedBy { it[featureSource] }.toFeature(clock) }

    override fun upsert(features: List<FeatureToUpsert>) {
        val existingFeatures = mutableMapOf<String, String>()
        Features.select { name inList features.map { it.name.value } }
            .forEach {
                existingFeatures["${it[featureSource]}-${it[name]}"] = it[id].value
            }

        features.forEach { feature ->
            val featureId: String? = existingFeatures["${feature.source.value}-${feature.name.value}"]
            if (featureId != null) {
                updateFeature(feature, featureId)
            } else {
                insertFeature(feature)
            }
        }
    }

    private fun insertFeature(feature: FeatureToUpsert) {
        val featureId = UUID.randomUUID().toString()
        Features.insert {
            it[id] = featureId
            it[creationTime] = clock.toUtcLocalDateTime()
            it[updateTime] = clock.toUtcLocalDateTime()
            it[name] = feature.name.value
            it[description] = feature.description
            it[featureSource] = feature.source.value
            it[component] = feature.component.value
        }
    }

    private fun updateFeature(feature: FeatureToUpsert, featureId: String) {
        Features.update({ id eq featureId }) {
            it[updateTime] = clock.toUtcLocalDateTime()
            it[name] = feature.name.value
            it[description] = feature.description
            it[featureSource] = feature.source.value
            it[component] = feature.component.value
        }
    }

    override fun delete(features: List<FeatureToDelete>) {
        if (features.isEmpty()) return
        val selectFeatures = features.map { it.searchExpression() }.compoundOr()
        Features.deleteWhere { selectFeatures }
    }
}
