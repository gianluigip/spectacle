package io.gianluigip.spectacle.common.fixtures

import io.gianluigip.spectacle.report.publisher.central.getSHA256Hash
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

val CREATION_TIME = Clock.System.now()

fun aWikiRequest(
    title: String = "Test Wiki",
    path: String = "/test",
    fileName: String = "test.md",
    content: String = "",
    checksum: String = getSHA256Hash(content),
    team: String = "SpectacleTeam",
    tags: List<String> = listOf(),
    features: List<String> = listOf(),
    source: String = "spectacle-test",
    component: String = "Spectacle Test",
) = WikiPageRequest(
    title = title,
    path = path,
    fileName = fileName,
    content = content,
    checksum = checksum,
    team = team,
    tags = tags,
    features = features,
    source = source,
    component = component,
)

fun aWikiMetadata(
    id: String = "1234",
    title: String = "Test Wiki",
    path: String = "/test",
    fileName: String = "test.md",
    checksum: String = "12345",
    team: String = "SpectacleTeam",
    tags: List<String> = listOf(),
    features: List<String> = listOf(),
    source: String = "spectacle-test",
    component: String = "Spectacle Test",
    creationTime: Instant = CREATION_TIME,
    updateTime: Instant = CREATION_TIME,
) = WikiPageMetadataResponse(
    id, title, path, fileName, checksum, team, tags, features, source, component, creationTime, updateTime
)