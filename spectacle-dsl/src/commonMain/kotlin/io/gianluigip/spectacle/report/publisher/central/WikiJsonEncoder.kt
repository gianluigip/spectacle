package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import kotlinx.datetime.Instant

internal fun WikiPageRequest.encodeToJson() = """
    {
        "title": "${title.escape()}",
        "path": "${path.escape()}",
        "fileName": "${fileName.escape()}",
        "content": "${content.escape()}",
        "checksum": "${checksum.escape()}",
        "team": "${team.escape()}",
        "tags": [ ${tags.joinToString(", ") { "\"${it.escape()}\"" }} ],
        "features": [ ${features.joinToString(", ") { "\"${it.escape()}\"" }} ],
        "source": "${source.escape()}",
        "component": "${component.escape()}"
    }    
""".trimIndent()

internal fun String.decodeToWikiPages(): List<WikiPageMetadataResponse> =
    decodeArrayOfObjects().map { it.decodeToWikiPage() }

internal fun String.decodeToWikiPage(): WikiPageMetadataResponse {
    val map = extractMapFromJson(this)
    return WikiPageMetadataResponse(
        id = map["id"]!!,
        title = map["title"]!!,
        path = map["path"]!!,
        fileName = map["fileName"]!!,
        checksum = map["checksum"]!!,
        team = map["team"]!!,
        tags = map["tags"]!!.decodeArrayOfStrings(),
        features = map["features"]!!.decodeArrayOfStrings(),
        source = map["source"]!!,
        component = map["component"]!!,
        creationTime = Instant.parse(map["creationTime"]!!),
        updateTime = Instant.parse(map["updateTime"]!!),
    )
}
