package io.gianluigip.spectacle.wiki.repository.tables

import io.gianluigip.spectacle.common.repository.StringIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object WikiPages : StringIdTable(name = "wiki_pages") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val title = varchar("title", 1000)
    val path = varchar("path", 1000)
    val fileName = varchar("file_name", 1000)
    val content = text("content")
    val checksum = varchar("checksum", 1000)
    val team = varchar("team", 500)
    val pageSource = varchar("source", 500)
    val component = varchar("component", 500)
}

object WikiPageFeatures : StringIdTable(name = "wiki_page_features") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val wikiId = varchar("wiki_id", 100).references(WikiPages.id)
    val name = varchar("name", 1000)
}

object WikiPageTags : StringIdTable(name = "wiki_page_tags") {
    val creationTime = datetime("creation_time")
    val updateTime = datetime("update_time")
    val wikiId = varchar("wiki_id", 100).references(WikiPages.id)
    val name = varchar("name", 1000)
}