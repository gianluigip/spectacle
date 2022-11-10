package io.gianluigip.spectacle.common.utils.api

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.dsl.interactions.receivesDeleteRequest
import io.gianluigip.spectacle.dsl.interactions.receivesGetRequest
import io.gianluigip.spectacle.dsl.interactions.receivesPostRequest
import io.gianluigip.spectacle.dsl.interactions.receivesPutRequest
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import kotlinx.coroutines.runBlocking

fun BaseIntegrationTest.getWiki(
    searchText: String? = null,
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): List<WikiPageMetadataResponse> = runBlocking {
    receivesGetRequest(
        path = "/api/wiki",
        queryParameters = mapOf(
            "searchText" to searchText,
            "features" to feature,
            "sources" to source,
            "components" to component,
            "tags" to tag,
            "teams" to team,
        ).filter { it.value != null }.mapValues { it.value!! }
    ).body()
}

fun BaseIntegrationTest.getWikiPage(wikiId: String): WikiPageResponse? = runBlocking {
    try {
        receivesGetRequest(path = "/api/wiki/${wikiId}").body()
    } catch (ex: NoTransformationFoundException) {
        null
    }
}

fun BaseIntegrationTest.postWikiPage(wikiPage: WikiPageRequest): WikiPageMetadataResponse = runBlocking {
    receivesPostRequest(
        path = "/api/wiki",
        body = wikiPage
    ).body()
}

fun BaseIntegrationTest.putWikiPage(wikiId: String, wikiPage: WikiPageRequest): WikiPageMetadataResponse = runBlocking {
    receivesPutRequest(
        path = "/api/wiki/${wikiId}",
        body = wikiPage
    ).body()
}

fun BaseIntegrationTest.deleteWikiPage(wikiId: String) = runBlocking {
    receivesDeleteRequest(path = "/api/wiki/${wikiId}")
}
