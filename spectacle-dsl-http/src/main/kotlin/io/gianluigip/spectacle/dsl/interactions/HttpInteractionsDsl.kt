package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.dsl.interactions.HttpInteractionsConfig.host
import io.gianluigip.spectacle.report.config.ConfigLoader
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.TextContent
import io.ktor.http.contentType

suspend fun receivesGetRequest(
    path: String,
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
) = receivesRequest(path, HttpMethod.Get, queryParameters, contentType, body, fromComponent, httpClient)

suspend fun receivesPutRequest(
    path: String,
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
) = receivesRequest(path, HttpMethod.Put, queryParameters, contentType, body, fromComponent, httpClient)

suspend fun receivesPostRequest(
    path: String,
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
) = receivesRequest(path, HttpMethod.Post, queryParameters, contentType, body, fromComponent, httpClient)

suspend fun receivesDeleteRequest(
    path: String,
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
) = receivesRequest(path, HttpMethod.Delete, queryParameters, contentType, body, fromComponent, httpClient)

suspend fun receivesRequest(
    path: String,
    httpMethod: HttpMethod,
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
): HttpResponse {
    val response = httpClient.request(generateUrl(path)) {
        method = httpMethod
        queryParameters.forEach {
            parameter(it.key, it.value)
        }
        if (body != null) {
            contentType(contentType)
            setBody(body)
        }
    }
    val metadata = HttpInteractionMetadata(
        path = path,
        method = httpMethod.value,
        queryParameters = queryParameters,
        requestContentType = response.request.contentType()?.toString(),
        requestBody = (response.request.content as? TextContent)?.text,
        responseContentType = response.contentType()?.toString(),
        responseBody = response.bodyAsText()
    )
    receivesRequestFrom(fromComponent, metadata)
    return response
}

fun receivesRequestFrom(
    fromComponent: String = ConfigLoader.CONFIG.component,
    metadata: HttpInteractionMetadata,
) = addHttpInteraction(fromComponent, metadata)

private fun generateUrl(path: String) = when {
    host.isEmpty() -> path
    host.endsWith("/") -> "$host$path"
    path.startsWith("/") -> "$host$path"
    else -> "$host/$path"
}

private fun addHttpInteraction(fromComponent: String, metadata: HttpInteractionMetadata) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.INBOUND,
            type = InteractionType.HTTP,
            name = fromComponent,
            metadata = metadata.toMap()
        )
    )
}
