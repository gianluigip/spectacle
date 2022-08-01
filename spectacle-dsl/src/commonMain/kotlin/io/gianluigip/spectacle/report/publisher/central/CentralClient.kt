package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest

interface CentralClient {

    suspend fun putSpecs(requestBody: SpecificationsToUpdateRequest)
    suspend fun getWikiPages(source: String): List<WikiPageMetadataResponse>
    suspend fun postWikiPage(wikiPage: WikiPageRequest)
    suspend fun putWikiPage(wikiPage: WikiPageRequest, wikiId: String)
    suspend fun deleteWikiPage(wikiId: String)

}
