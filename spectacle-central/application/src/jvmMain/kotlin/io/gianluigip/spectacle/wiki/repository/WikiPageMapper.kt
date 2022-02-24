package io.gianluigip.spectacle.wiki.repository

import io.gianluigip.spectacle.common.utils.fromUtc
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.toComponent
import io.gianluigip.spectacle.specification.model.toFeature
import io.gianluigip.spectacle.specification.model.toSource
import io.gianluigip.spectacle.specification.model.toTeam
import io.gianluigip.spectacle.wiki.model.WikiId
import io.gianluigip.spectacle.wiki.model.WikiPage
import io.gianluigip.spectacle.wiki.model.WikiPageMetadata
import io.gianluigip.spectacle.wiki.model.WikiPageToUpsert
import io.gianluigip.spectacle.wiki.model.toWikiId
import io.gianluigip.spectacle.wiki.repository.tables.WikiPageFeatures
import io.gianluigip.spectacle.wiki.repository.tables.WikiPageTags
import io.gianluigip.spectacle.wiki.repository.tables.WikiPages
import org.jetbrains.exposed.sql.ResultRow
import java.time.Clock
import java.time.ZonedDateTime

fun List<ResultRow>.toWikiPage(clock: Clock): WikiPage? {
    if (isEmpty()) return null
    return first().run { toWikiPage(clock, tags = extractTags(), features = extractFeatures()) }
}

fun List<ResultRow>.toWikiPageMetaData(clock: Clock): WikiPageMetadata {
    return first().run { toWikiPageMetaData(clock, tags = extractTags(), features = extractFeatures()) }
}

private fun List<ResultRow>.extractFeatures() =
    distinctBy { it[WikiPageFeatures.id] }
        .filter { it[WikiPageFeatures.name] != null }
        .map { it[WikiPageFeatures.name].toFeature() }
        .sortedBy { it.value }

private fun List<ResultRow>.extractTags() =
    distinctBy { it[WikiPageTags.id] }
        .filter { it[WikiPageTags.name] != null }
        .map { TagName(it[WikiPageTags.name]) }
        .sortedBy { it.value }

private fun ResultRow.toWikiPage(
    clock: Clock,
    tags: List<TagName>,
    features: List<FeatureName>,
) = WikiPage(
    id = get(WikiPages.id).value.toWikiId(),
    title = get(WikiPages.title),
    content = get(WikiPages.content),
    path = get(WikiPages.path),
    fileName = get(WikiPages.fileName),
    checksum = get(WikiPages.checksum),
    team = get(WikiPages.team).toTeam(),
    tags = tags,
    features = features,
    source = get(WikiPages.pageSource).toSource(),
    component = get(WikiPages.component).toComponent(),
    creationTime = get(WikiPages.creationTime).fromUtc(clock),
    updateTime = get(WikiPages.updateTime).fromUtc(clock),
)

private fun ResultRow.toWikiPageMetaData(
    clock: Clock,
    tags: List<TagName>,
    features: List<FeatureName>,
) = WikiPageMetadata(
    id = get(WikiPages.id).value.toWikiId(),
    title = get(WikiPages.title),
    path = get(WikiPages.path),
    fileName = get(WikiPages.fileName),
    checksum = get(WikiPages.checksum),
    team = get(WikiPages.team).toTeam(),
    tags = tags,
    features = features,
    source = get(WikiPages.pageSource).toSource(),
    component = get(WikiPages.component).toComponent(),
    creationTime = get(WikiPages.creationTime).fromUtc(clock),
    updateTime = get(WikiPages.updateTime).fromUtc(clock),
)

fun WikiPageToUpsert.toMetadata(
    id: WikiId,
    creationTime: ZonedDateTime,
    updateTime: ZonedDateTime,
) = WikiPageMetadata(
    id, title, path, fileName, checksum, team, tags, features, source, component, creationTime, updateTime
)
