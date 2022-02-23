package io.gianluigip.spectacle.wiki.model

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import java.time.ZonedDateTime

@JvmInline
value class WikiId(val value: String) {
    override fun toString() = value
}

fun String.toWikiId() = WikiId(this)

data class WikiPageToUpsert(
    val title: String,
    val path: String,
    val content: String,
    val checksum: String,
    val team: TeamName,
    val tags: List<TagName>,
    val features: List<FeatureName>,
    val source: Source,
    val component: Component,
)

data class WikiPageMetadata(
    val id: WikiId,
    val title: String,
    val path: String,
    val checksum: String,
    val team: TeamName,
    val tags: List<TagName>,
    val features: List<FeatureName>,
    val source: Source,
    val component: Component,
    val creationTime: ZonedDateTime,
    val updateTime: ZonedDateTime,
)

data class WikiPage(
    val id: WikiId,
    val title: String,
    val path: String,
    val content: String,
    val checksum: String,
    val team: TeamName,
    val tags: List<TagName>,
    val features: List<FeatureName>,
    val source: Source,
    val component: Component,
    val creationTime: ZonedDateTime,
    val updateTime: ZonedDateTime,
)
