package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.CentralPublisherConfig
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

class JvmCentralClient(
    config: CentralPublisherConfig
) : CentralClient {

    private val jsonEncoder = Json { encodeDefaults = true }
    private val host = config.host?.value.let {
        when {
            it == null -> ""
            it.endsWith("/") -> it
            else -> "$it/"
        }
    }
    private val encodedCredential = Base64.getEncoder().encodeToString("${config.username}:${config.password}".encodeToByteArray())

    override suspend fun putSpecs(requestBody: SpecificationsToUpdateRequest) = withContext(Dispatchers.IO) {

        val centralUrl = "${host}api/specification"
        println("Publishing the specs to $centralUrl")

        try {
            val content = requestBody.encodeToJson()
            val httpClient = clientFor("PUT", centralUrl, content)
            val requestStatus = httpClient.responseCode
            val responseBody = httpClient.responseAsText()
            println("Publishing to Central finished with status $requestStatus and response $responseBody")

        } catch (exception: Exception) {
            println("Central Publisher failed trying to communicate with the server: ${exception.message}")
            exception.printStackTrace()
        }
    }

    override suspend fun getWikiPages(source: String): List<WikiPageMetadataResponse> = withContext(Dispatchers.IO) {
        val wikisEncoded = clientFor("GET", "${host}api/wiki?sources=$source").responseAsText()
        return@withContext jsonEncoder.decodeFromString(wikisEncoded)
    }

    override suspend fun postWikiPage(wikiPage: WikiPageRequest): Unit = withContext(Dispatchers.IO) {
        val content = jsonEncoder.encodeToString(wikiPage)
        clientFor("POST", "${host}api/wiki", content).responseAsText()
    }

    override suspend fun putWikiPage(wikiPage: WikiPageRequest, wikiId: String): Unit = withContext(Dispatchers.IO) {
        val content = jsonEncoder.encodeToString(wikiPage)
        clientFor("PUT", "${host}api/wiki/${wikiId}", content).responseAsText()
    }

    override suspend fun deleteWikiPage(wikiId: String): Unit = withContext(Dispatchers.IO) {
        clientFor("DELETE", "${host}api/wiki/${wikiId}").responseAsText()
    }

    private fun clientFor(method: String, url: String, content: String? = null) =
        (URL(url).openConnection() as HttpURLConnection)
            .apply {
                requestMethod = method
                setRequestProperty("Authorization", "Basic $encodedCredential")
                if (content != null) {
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Content-Length", content.length.toString())
                    doOutput = true
                    DataOutputStream(outputStream).use { it.writeBytes(content) }
                }
            }

    private fun HttpURLConnection.responseAsText(): String {
        val response = StringBuilder()
        BufferedReader(InputStreamReader(inputStream)).use { bf ->
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                response.append(line)
            }
        }
        return response.toString()
    }
}
