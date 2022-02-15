package io.gianluigip.spectacle.specification.repository

import io.gianluigip.spectacle.common.utils.toUtcLocalDateTime
import io.gianluigip.spectacle.specification.SpecificationRepository
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecToUpsert
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.repository.tables.Specifications.name
import io.gianluigip.spectacle.specification.repository.tables.Specifications.specSource
import io.gianluigip.spectacle.specification.repository.tables.Tags
import io.gianluigip.spectacle.specification.repository.tables.searchExpression
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.compoundOr
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.Clock
import java.util.UUID
import io.gianluigip.spectacle.specification.repository.tables.SpecificationSteps as Steps
import io.gianluigip.spectacle.specification.repository.tables.Specifications as Specs

class ExposedSpecificationRepository(
    private val clock: Clock,
) : SpecificationRepository {

    override fun findAll(): List<Specification> = findBy(null)

    override fun findBy(
        features: Set<FeatureName>?,
        sources: Set<Source>?,
        components: Set<Component>?,
        tags: Set<TagName>?,
        teams: Set<TeamName>?,
        statuses: Set<SpecStatus>?,
    ): List<Specification> {
        val query = Specs
            .join(Steps, JoinType.LEFT, additionalConstraint = { Specs.id eq Steps.specId })
            .join(Tags, JoinType.LEFT, additionalConstraint = { Specs.id eq Tags.specId })
            .selectAll()
        if (features?.isNotEmpty() == true) query.andWhere { Specs.feature inList features.map { it.value } }
        if (sources?.isNotEmpty() == true) query.andWhere { specSource inList sources.map { it.value } }
        if (components?.isNotEmpty() == true) query.andWhere { Specs.component inList components.map { it.value } }
        if (teams?.isNotEmpty() == true) query.andWhere { Specs.team inList teams.map { it.value } }
        if (statuses?.isNotEmpty() == true) query.andWhere { Specs.status inList statuses.map { it.name } }
        if (tags?.isNotEmpty() == true) query.andWhere { Tags.name inList tags.map { it.value } }
        return query.toSpecsWithRelations()
    }

    /**
     * Find all the specs for a given source without loading the steps and tags.
     */
    override fun findBySource(source: Source): List<Specification> =
        Specs.select { specSource eq source.value }.orderBy(name to SortOrder.ASC).map { it.toSpec(clock, listOf()) }

    private fun findSpecIds(specs: List<Specification>): List<String> {
        val selectAllSpecs = specs.map { it.searchExpression() }.compoundOr()
        return Specs.select(selectAllSpecs).map { it[Specs.id].value }
    }

    private fun Query.toSpecsWithRelations() = orderBy(name to SortOrder.ASC)
        .groupBy { it[Specs.id].value }.values.map { it.sortedBy { it[Steps.index] }.toSpec(clock) }

    override fun upsert(specs: List<SpecToUpsert>) {
        val existingSpecs = mutableMapOf<String, String>()
        Specs.select { name inList specs.map { it.name.value } }
            .forEach {
                existingSpecs["${it[specSource]}-${it[name]}"] = it[Specs.id].value
            }

        specs.forEach { spec ->
            val specId: String? = existingSpecs["${spec.source.value}-${spec.name.value}"]
            if (specId != null) {
                updateSpec(spec, specId)
            } else {
                insertSpec(spec)
            }
        }
    }

    private fun insertSpec(spec: SpecToUpsert) {
        val specId = UUID.randomUUID().toString()
        Specs.insert {
            it[id] = specId
            it[creationTime] = clock.toUtcLocalDateTime()
            it[updateTime] = clock.toUtcLocalDateTime()
            it[name] = spec.name.value
            it[feature] = spec.feature.value
            it[team] = spec.team.value
            it[specSource] = spec.source.value
            it[component] = spec.component.value
            it[status] = spec.status.name
        }
        insertSteps(spec.steps, specId)
        insertTags(spec, specId)
    }

    private fun insertSteps(steps: List<SpecificationStep>, specId: String) {
        Steps.batchInsert(steps, shouldReturnGeneratedValues = false) {
            val stepId = UUID.randomUUID().toString()
            this[Steps.id] = stepId
            this[Steps.creationTime] = clock.toUtcLocalDateTime()
            this[Steps.updateTime] = clock.toUtcLocalDateTime()
            this[Steps.specId] = specId
            this[Steps.type] = it.type.name
            this[Steps.description] = it.description
            this[Steps.index] = it.index
        }
    }

    private fun insertTags(spec: SpecToUpsert, specId: String) {
        Tags.batchInsert(spec.tags, shouldReturnGeneratedValues = false) {
            val tagId = UUID.randomUUID().toString()
            this[Tags.id] = tagId
            this[Tags.creationTime] = clock.toUtcLocalDateTime()
            this[Tags.updateTime] = clock.toUtcLocalDateTime()
            this[Tags.specId] = specId
            this[Tags.name] = it.value
            this[Tags.teamName] = spec.team.value
            this[Tags.tagSource] = spec.source.value
            this[Tags.component] = spec.component.value
        }
    }

    private fun updateSpec(spec: SpecToUpsert, specId: String) {
        Specs.update({ Specs.id eq specId }) {
            it[updateTime] = clock.toUtcLocalDateTime()
            it[name] = spec.name.value
            it[feature] = spec.feature.value
            it[team] = spec.team.value
            it[specSource] = spec.source.value
            it[component] = spec.component.value
            it[status] = spec.status.name
        }
        deleteSteps(specId)
        deleteTags(specId)
        insertSteps(spec.steps, specId)
        insertTags(spec, specId)
    }

    override fun delete(specs: List<Specification>) {
        if (specs.isEmpty()) return
        val ids = findSpecIds(specs)
        Steps.deleteWhere { Steps.specId inList ids }
        Tags.deleteWhere { Tags.specId inList ids }
        Specs.deleteWhere { Specs.id inList ids }
    }

    private fun deleteSteps(specId: String) = Steps.deleteWhere { Steps.specId eq specId }

    private fun deleteTags(specId: String) = Tags.deleteWhere { Tags.specId eq specId }

    fun deleteAll() {
        Steps.deleteAll()
        Tags.deleteAll()
        Specs.deleteAll()
    }
}
