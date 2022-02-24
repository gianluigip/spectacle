package io.gianluigip.spectacle.common.fixtures

import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

val CREATION_TIME = Clock.System.now()

fun aWikiMetadata(
    id: String = "1234",
    title: String = "Test Wiki",
    path: String = "/test",
    fileName: String = "test.md",
    checksum: String = "12345",
    team: String = "Team1",
    tags: List<String> = listOf("Tag1", "Tag2"),
    features: List<String> = listOf("Feature1", "Feature2"),
    source: String = "Source1",
    component: String = "Component1",
    creationTime: Instant = CREATION_TIME,
    updateTime: Instant = CREATION_TIME,
) = WikiPageMetadataResponse(
    id, title, path, fileName, checksum, team, tags, features, source, component, creationTime, updateTime
)