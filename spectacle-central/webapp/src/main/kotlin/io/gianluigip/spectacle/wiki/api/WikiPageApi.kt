package io.gianluigip.spectacle.wiki.api

import io.gianluigip.spectacle.common.api.API_CLIENT
import io.gianluigip.spectacle.common.api.ENDPOINT
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import io.gianluigip.spectacle.wiki.components.WikiBrowserFilters
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

suspend fun getAllPages(
    filters: WikiBrowserFilters
) = getAllPages(
    searchText = filters.searchText
)

suspend fun getAllPages(
    searchText: String? = null,
    feature: String? = null,
    source: String? = null,
    component: String? = null,
    tag: String? = null,
    team: String? = null,
): List<WikiPageMetadataResponse> {
    return API_CLIENT.get("$ENDPOINT/api/wiki") {
        searchText?.let { parameter("searchText", searchText) }
        feature?.let { parameter("features", feature) }
        source?.let { parameter("sources", source) }
        component?.let { parameter("components", component) }
        tag?.let { parameter("tags", tag) }
        team?.let { parameter("teams", team) }
    }.body()
}

suspend fun getWikiPage(wikiId: String): WikiPageResponse? {
    return try {
        API_CLIENT.get("$ENDPOINT/api/wiki/${wikiId}").body()
    } catch (ex: ClientRequestException) {
        null
    }
}
