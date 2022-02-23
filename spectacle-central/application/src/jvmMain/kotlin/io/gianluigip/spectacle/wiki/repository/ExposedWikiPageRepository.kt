package io.gianluigip.spectacle.wiki.repository

import io.gianluigip.spectacle.common.utils.toUtcLocalDateTime
import io.gianluigip.spectacle.common.utils.toZonedDateTime
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.wiki.WikiPageRepository
import io.gianluigip.spectacle.wiki.model.WikiId
import io.gianluigip.spectacle.wiki.model.WikiPage
import io.gianluigip.spectacle.wiki.model.WikiPageMetadata
import io.gianluigip.spectacle.wiki.model.WikiPageToUpsert
import io.gianluigip.spectacle.wiki.model.toWikiId
import io.gianluigip.spectacle.wiki.repository.tables.WikiPageFeatures
import io.gianluigip.spectacle.wiki.repository.tables.WikiPageTags
import io.gianluigip.spectacle.wiki.repository.tables.WikiPages
import io.gianluigip.spectacle.wiki.repository.tables.WikiPages.title
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

class ExposedWikiPageRepository(
    private val clock: Clock,
) : WikiPageRepository {

    override fun findById(id: WikiId): WikiPage? =
        WikiPages
            .join(WikiPageFeatures, JoinType.LEFT, additionalConstraint = { WikiPages.id eq WikiPageFeatures.wikiId })
            .join(WikiPageTags, JoinType.LEFT, additionalConstraint = { WikiPageFeatures.id eq WikiPageTags.wikiId })
            .select { WikiPages.id eq id.value }
            .toList().toWikiPage(clock)

    override fun findBy(
        features: Set<FeatureName>?,
        sources: Set<Source>?,
        components: Set<Component>?,
        tags: Set<TagName>?,
        teams: Set<TeamName>?
    ): List<WikiPageMetadata> {
        val query = WikiPages
            .join(WikiPageFeatures, JoinType.LEFT, additionalConstraint = { WikiPages.id eq WikiPageFeatures.wikiId })
            .join(WikiPageTags, JoinType.LEFT, additionalConstraint = { WikiPageFeatures.id eq WikiPageTags.wikiId })
            .slice(
                WikiPages.id, title, WikiPages.path, WikiPages.checksum, WikiPages.team, WikiPageFeatures.id,
                WikiPageFeatures.name, WikiPageTags.id, WikiPageTags.name, WikiPages.pageSource, WikiPages.component,
                WikiPages.creationTime, WikiPages.updateTime
            ).selectAll()
        if (sources?.isNotEmpty() == true) query.andWhere { WikiPages.pageSource inList sources.map { it.value } }
        if (components?.isNotEmpty() == true) query.andWhere { WikiPages.component inList components.map { it.value } }
        if (teams?.isNotEmpty() == true) query.andWhere { WikiPages.team inList teams.map { it.value } }
        if (features?.isNotEmpty() == true) query.andWhere { WikiPageFeatures.name inList features.map { it.value } }
        if (tags?.isNotEmpty() == true) query.andWhere { WikiPageTags.name inList tags.map { it.value } }
        return query.toPagesMetadataWithRelations()
    }


    private fun Query.toPagesMetadataWithRelations() = orderBy(title to SortOrder.ASC)
        .groupBy { it[WikiPages.id].value }.values.map { it.toWikiPageMetaData(clock) }

    override fun save(wiki: WikiPageToUpsert): WikiPageMetadata {
        val wikiId = UUID.randomUUID().toString()
        val now = clock.toUtcLocalDateTime()
        val zonedNow = clock.toZonedDateTime()
        WikiPages.insert {
            it[WikiPages.id] = wikiId
            it[title] = wiki.title
            it[content] = wiki.content
            it[path] = wiki.path
            it[checksum] = wiki.checksum
            it[team] = wiki.team.value
            it[pageSource] = wiki.source.value
            it[component] = wiki.component.value
            it[creationTime] = now
            it[updateTime] = now
        }
        insertTags(wiki, wikiId, now)
        insertFeatures(wiki, wikiId, now)
        return wiki.toMetadata(wikiId.toWikiId(), zonedNow, zonedNow)
    }

    override fun update(id: WikiId, wiki: WikiPageToUpsert): WikiPageMetadata {
        val now = clock.toUtcLocalDateTime()
        val zonedNow = clock.toZonedDateTime()
        WikiPages.update({ WikiPages.id eq id.value }) {
            it[WikiPages.id] = id.value
            it[title] = wiki.title
            it[content] = wiki.content
            it[path] = wiki.path
            it[checksum] = wiki.checksum
            it[team] = wiki.team.value
            it[pageSource] = wiki.source.value
            it[component] = wiki.component.value
            it[updateTime] = now
        }
        deleteTags(id)
        deleteFeatures(id)
        insertTags(wiki, id.value, now)
        insertFeatures(wiki, id.value, now)
        return wiki.toMetadata(id, zonedNow, zonedNow)
    }

    private fun insertTags(wiki: WikiPageToUpsert, wikiId: String, now: LocalDateTime) {
        WikiPageTags.batchInsert(wiki.tags, shouldReturnGeneratedValues = false) {
            val tagId = UUID.randomUUID().toString()
            this[WikiPageTags.id] = tagId
            this[WikiPageTags.wikiId] = wikiId
            this[WikiPageTags.name] = it.value
            this[WikiPageTags.creationTime] = now
            this[WikiPageTags.updateTime] = now
        }
    }

    private fun insertFeatures(wiki: WikiPageToUpsert, wikiId: String, now: LocalDateTime) {
        WikiPageFeatures.batchInsert(wiki.features, shouldReturnGeneratedValues = false) {
            val featureId = UUID.randomUUID().toString()
            this[WikiPageFeatures.id] = featureId
            this[WikiPageFeatures.wikiId] = wikiId
            this[WikiPageFeatures.name] = it.value
            this[WikiPageFeatures.creationTime] = now
            this[WikiPageFeatures.updateTime] = now
        }
    }

    override fun delete(id: WikiId) {
        deleteTags(id)
        deleteFeatures(id)
        WikiPages.deleteWhere { WikiPages.id eq id.value }
    }

    private fun deleteTags(id: WikiId) = WikiPageTags.deleteWhere { WikiPageTags.wikiId eq id.value }
    private fun deleteFeatures(id: WikiId) = WikiPageFeatures.deleteWhere { WikiPageFeatures.wikiId eq id.value }

    fun deleteAll() {
        WikiPageTags.deleteAll()
        WikiPageFeatures.deleteAll()
        WikiPages.deleteAll()
    }
}