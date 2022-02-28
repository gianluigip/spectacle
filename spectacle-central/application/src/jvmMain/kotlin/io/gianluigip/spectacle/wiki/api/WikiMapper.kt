package io.gianluigip.spectacle.wiki.api

import io.gianluigip.spectacle.common.utils.toKotlinInstant
import io.gianluigip.spectacle.specification.model.toComponent
import io.gianluigip.spectacle.specification.model.toFeature
import io.gianluigip.spectacle.specification.model.toSource
import io.gianluigip.spectacle.specification.model.toTag
import io.gianluigip.spectacle.specification.model.toTeam
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import io.gianluigip.spectacle.wiki.model.WikiPage
import io.gianluigip.spectacle.wiki.model.WikiPageMetadata
import io.gianluigip.spectacle.wiki.model.WikiPageToUpsert

fun WikiPageRequest.toModel() = WikiPageToUpsert(
    title = title,
    path = path,
    fileName = fileName,
    content = content,
    checksum = checksum,
    team = team.toTeam(),
    tags = tags.map { it.toTag() },
    features = features.map { it.toFeature() },
    source = source.toSource(),
    component = component.toComponent(),
)

fun WikiPageMetadata.toResponse() = WikiPageMetadataResponse(
    id = id.value,
    title = title,
    path = path,
    fileName = fileName,
    checksum = checksum,
    team = team.value,
    tags = tags.map { it.value },
    features = features.map { it.value },
    source = source.value,
    component = component.value,
    creationTime = creationTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
)

fun WikiPage.toResponse() = WikiPageResponse(
    id = id.value,
    title = title,
    path = path,
    fileName = fileName,
    content = content,
    checksum = checksum,
    team = team.value,
    tags = tags.map { it.value },
    features = features.map { it.value },
    source = source.value,
    component = component.value,
    creationTime = creationTime.toKotlinInstant(),
    updateTime = updateTime.toKotlinInstant(),
)