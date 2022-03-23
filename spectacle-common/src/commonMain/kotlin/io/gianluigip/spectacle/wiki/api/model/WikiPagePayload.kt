package io.gianluigip.spectacle.wiki.api.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class WikiPageRequest(
    val title: String,
    val path: String,
    val fileName: String,
    val content: String,
    val checksum: String,
    val team: String,
    val tags: List<String>,
    val features: List<String>,
    val source: String,
    val component: String,
) {
    fun fullPath() = "${if (path.startsWith("/")) "" else "/"}$path${if (path.endsWith("/")) "" else "/"}$fileName"
}

@Serializable
data class WikiPageMetadataResponse(
    val id: String,
    val title: String,
    val path: String,
    val fileName: String,
    val checksum: String,
    val team: String,
    val tags: List<String>,
    val features: List<String>,
    val source: String,
    val component: String,
    val creationTime: Instant,
    val updateTime: Instant,
) {
    fun fullPath() = "${if (path.startsWith("/")) "" else "/"}$path${if (path.endsWith("/")) "" else "/"}$fileName"
}

@Serializable
data class WikiPageResponse(
    val id: String,
    val title: String,
    val path: String,
    val fileName: String,
    val content: String,
    val checksum: String,
    val team: String,
    val tags: List<String>,
    val features: List<String>,
    val source: String,
    val component: String,
    val creationTime: Instant,
    val updateTime: Instant,
)
