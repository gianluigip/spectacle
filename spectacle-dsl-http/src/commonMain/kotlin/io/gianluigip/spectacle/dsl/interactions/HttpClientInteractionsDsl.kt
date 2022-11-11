package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.dsl.interactions.HttpInteractionsConfig.host
import io.gianluigip.spectacle.report.config.ConfigLoader
import io.gianluigip.spectacle.specification.model.HttpInteractionMetadata
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
    pathParameters: Map<String, String> = emptyMap(),
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
) = receivesRequest(path, HttpMethod.Get, pathParameters, queryParameters, contentType, body, fromComponent, httpClient)

suspend fun receivesPutRequest(
    path: String,
    pathParameters: Map<String, String> = emptyMap(),
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
) = receivesRequest(path, HttpMethod.Put, pathParameters, queryParameters, contentType, body, fromComponent, httpClient)

suspend fun receivesPostRequest(
    path: String,
    pathParameters: Map<String, String> = emptyMap(),
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
) = receivesRequest(path, HttpMethod.Post, pathParameters, queryParameters, contentType, body, fromComponent, httpClient)

suspend fun receivesDeleteRequest(
    path: String,
    pathParameters: Map<String, String> = emptyMap(),
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
) = receivesRequest(path, HttpMethod.Delete, pathParameters, queryParameters, contentType, body, fromComponent, httpClient)

suspend fun receivesRequest(
    path: String,
    httpMethod: HttpMethod,
    pathParameters: Map<String, String> = emptyMap(),
    queryParameters: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
    body: Any? = null,
    fromComponent: String = ConfigLoader.CONFIG.component,
    httpClient: HttpClient = HttpInteractionsConfig.httpClient,
): HttpResponse {
    val response = httpClient.request(generateUrl(path, pathParameters)) {
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
        method = httpMethod.value.toUpperCase(),
        queryParameters = queryParameters,
        requestBody = body?.let { (response.request.content as? TextContent)?.text?.trim() },
        requestContentType = response.request.contentType()?.toString(),
        responseBody = response.bodyAsText().trim(),
        responseStatus = response.status.value.toString(),
        responseContentType = response.contentType()?.toString(),
    )
    receivesRequestFrom(fromComponent, metadata)
    return response
}

private fun generateUrl(path: String, pathParameters: Map<String, String>): String {
    var url = when {
        host.isEmpty() -> path
        host.endsWith("/") -> "$host$path"
        path.startsWith("/") -> "$host$path"
        else -> "$host/$path"
    }
    pathParameters.forEach {
        url = url.replace("{${it.key}}", it.value)
    }
    return url
}
