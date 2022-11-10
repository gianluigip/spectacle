package io.gianluigip.spectacle.common.utils.api

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.dsl.interactions.receivesRequestFrom
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

fun BaseIntegrationTest.getWiki(
    searchText: String? = null,
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): List<WikiPageMetadataResponse> = runBlocking {
    receivesRequestFromDSL()
    httpClient.get("$httpHost/api/wiki") {
        feature?.let { parameter("features", feature) }
        source?.let { parameter("sources", source) }
        component?.let { parameter("components", component) }
        tag?.let { parameter("tags", tag) }
        team?.let { parameter("teams", team) }
        searchText?.let { parameter("searchText", searchText) }
    }.body()
}

fun BaseIntegrationTest.getWikiPage(wikiId: String): WikiPageResponse? = runBlocking {
    receivesRequestFromDSL()
    try {
        httpClient.get("$httpHost/api/wiki/${wikiId}").body()
    } catch (ex: NoTransformationFoundException) {
        null
    }
}

fun BaseIntegrationTest.postWikiPage(wikiPage: WikiPageRequest): WikiPageMetadataResponse = runBlocking {
    receivesRequestFromDSL()
    httpClient.post("$httpHost/api/wiki") {
        contentType(ContentType.Application.Json)
        setBody(wikiPage)
    }.body()
}

fun BaseIntegrationTest.putWikiPage(wikiId: String, wikiPage: WikiPageRequest): WikiPageMetadataResponse = runBlocking {
    receivesRequestFromDSL()
    httpClient.put("$httpHost/api/wiki/${wikiId}") {
        contentType(ContentType.Application.Json)
        setBody(wikiPage)
    }.body()
}

fun BaseIntegrationTest.deleteWikiPage(wikiId: String) = runBlocking {
    receivesRequestFromDSL()
    httpClient.delete("$httpHost/api/wiki/${wikiId}")
}

fun receivesRequestFromDSL() = receivesRequestFrom("Spectacle DSL Publisher")