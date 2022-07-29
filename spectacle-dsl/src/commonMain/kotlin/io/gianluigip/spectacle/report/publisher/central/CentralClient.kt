package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.CentralPublisherConfig
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

class CentralClient(
    private val config: CentralPublisherConfig
) {

    private val host = config.host?.toString().let {
        when {
            it == null -> ""
            it.endsWith("/") -> it
            else -> "$it/"
        }
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) { json() }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = config.username, password = config.password)
                }
            }
        }
    }

    suspend fun postSpecs(requestBody: SpecificationsToUpdateRequest) {

        val centralUrl = "${host}api/specification"
        println("Publishing the specs to $centralUrl")

        try {
            val response = httpClient.put(centralUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            println("Publishing to Central finished with status ${response.status} and response ${response.bodyAsText()}")

        } catch (exception: Exception) {
            println("Central Publisher failed trying to communicate with the server: ${exception.message}")
            exception.printStackTrace()
        }
    }

    suspend fun getWikiPages(source: String): List<WikiPageMetadataResponse> {
        return httpClient.get("${host}api/wiki") {
            parameter("sources", source)
        }.body()
    }

    suspend fun postWikiPage(wikiPage: WikiPageRequest) {
        httpClient.post("${host}api/wiki") {
            contentType(ContentType.Application.Json)
            setBody(wikiPage)
        }
    }

    suspend fun putWikiPage(wikiPage: WikiPageRequest, wikiId: String) {
        httpClient.put("${host}api/wiki/${wikiId}") {
            contentType(ContentType.Application.Json)
            setBody(wikiPage)
        }
    }

    suspend fun deleteWikiPage(wikiId: String) {
        httpClient.delete("${host}api/wiki/${wikiId}")
    }
}
