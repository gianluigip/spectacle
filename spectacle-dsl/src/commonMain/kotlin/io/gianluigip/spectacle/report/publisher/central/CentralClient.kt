package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

object CentralClient {

    val HTTP_CLIENT = HttpClient {
        install(ContentNegotiation) { json() }
    }

    suspend fun getWikiPages(source: String, config: ReportConfiguration): List<WikiPageMetadataResponse> {
        return HTTP_CLIENT.get("${config.centralHost}api/wiki") {
            parameter("sources", source)
        }.body()
    }

    suspend fun postWikiPage(wikiPage: WikiPageRequest, config: ReportConfiguration) {
        HTTP_CLIENT.post("${config.centralHost}api/wiki") {
            contentType(ContentType.Application.Json)
            setBody(wikiPage)
        }
    }

    suspend fun putWikiPage(wikiPage: WikiPageRequest, wikiId: String, config: ReportConfiguration) {
        HTTP_CLIENT.put("${config.centralHost}api/wiki/${wikiId}") {
            contentType(ContentType.Application.Json)
            setBody(wikiPage)
        }
    }

    suspend fun deleteWikiPage(wikiId: String, config: ReportConfiguration) {
        HTTP_CLIENT.delete("${config.centralHost}api/wiki/${wikiId}")
    }
}
