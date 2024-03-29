package io.gianluigip.spectacle.specification.model

import io.gianluigip.spectacle.common.escape

data class HttpInteractionMetadata(
    val path: String,
    val method: String,
    val queryParameters: Map<String, String>,
    val requestBody: String?,
    val requestContentType: String?,
    val responseBody: String,
    val responseStatus: String,
    val responseContentType: String?,
) {
    companion object {
        fun isHttpMetadataSimilar(interaction1: SpecInteraction, interaction2: SpecInteraction) =
            interaction1.metadata["path"] == interaction2.metadata["path"] &&
                    interaction1.metadata["method"] == interaction2.metadata["method"] &&
                    interaction1.metadata["queryParameters"] == interaction2.metadata["queryParameters"] &&
                    interaction1.metadata["requestContentType"] == interaction2.metadata["requestContentType"] &&
                    interaction1.metadata["responseStatus"] == interaction2.metadata["responseStatus"] &&
                    interaction1.metadata["responseContentType"] == interaction2.metadata["responseContentType"]
    }

    fun toMap(): Map<String, String> = mapOf(
        "metadataType" to "HTTP",
        "path" to path,
        "method" to method,
        "queryParameters" to """{ ${queryParameters.map { (key, value) -> """ "${key.escape()}": "${value.escape()}" """ }.joinToString(", ")} }""",
        "requestContentType" to requestContentType,
        "requestBody" to requestBody,
        "responseContentType" to responseContentType,
        "responseBody" to responseBody,
        "responseStatus" to responseStatus,
    ).filter { it.value != null }.mapValues { it.value!! }

}

fun SpecInteraction.hasHttpMetadata() = metadata.contains("path") && metadata.contains("method") && metadata.contains("responseStatus")
