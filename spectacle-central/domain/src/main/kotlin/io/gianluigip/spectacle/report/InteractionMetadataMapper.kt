package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.specification.model.HttpInteractionMetadata
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.hasHttpMetadata
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun SpecInteraction.toHttpMetadata(): HttpInteractionMetadata {
    if (!hasHttpMetadata()) {
        throw IllegalArgumentException("This interaction $this does not contain HTTP Metadata.")
    }
    return try {
        HttpInteractionMetadata(
            path = metadata["path"]!!,
            method = metadata["method"]!!,
            queryParameters = metadata["queryParameters"]?.let { Json.decodeFromString(it) } ?: emptyMap(),
            requestBody = metadata["requestBody"],
            requestContentType = metadata["requestContentType"],
            responseBody = metadata["responseBody"] ?: "",
            responseStatus = metadata["responseStatus"]!!,
            responseContentType = metadata["responseContentType"],
        )
    } catch (ex: Exception) {
        throw IllegalArgumentException("Failed to parse HTTP Metadata for interaction $this: ${ex.message}.", ex)
    }
}
