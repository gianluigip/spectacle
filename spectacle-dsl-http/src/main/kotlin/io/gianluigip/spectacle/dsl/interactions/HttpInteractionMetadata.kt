package io.gianluigip.spectacle.dsl.interactions

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class HttpInteractionMetadata(
    val path: String,
    val method: String,
    val queryParameters: Map<String, String>,
    val requestContentType: String?,
    val requestBody: String?,
    val responseContentType: String?,
    val responseBody: String,
) {
    fun toMap(): Map<String, String> = mapOf(
        "path" to path,
        "method" to method,
        "queryParameters" to Json.encodeToString(queryParameters),
        "requestContentType" to requestContentType,
        "requestBody" to requestBody,
        "responseContentType" to responseContentType,
        "responseBody" to responseBody,
    ).filter { it.value != null }.mapValues { it.value!! }

    companion object {
        fun fromMap(map: Map<String, String>): HttpInteractionMetadata? {
            if (!map.contains("path") || !map.contains("method")) {
                return null
            }
            return try {
                HttpInteractionMetadata(
                    path = map["path"]!!,
                    method = map["method"]!!,
                    queryParameters = map["queryParameters"]?.let { Json.decodeFromString(it) } ?: emptyMap(),
                    requestContentType = map["requestContentType"],
                    requestBody = map["requestBody"],
                    responseContentType = map["responseContentType"],
                    responseBody = map["responseBody"] ?: "",
                )
            } catch (ex: Exception) {
                null
            }
        }
    }
}
