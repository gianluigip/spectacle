package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.specification.api.model.FeatureToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecificationStep

internal fun String.escape() =
    replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\b", "\\b")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")


internal fun SpecificationsToUpdateRequest.encodeToJson(): String = """
        {
            "source": "${source.escape()}",
            "component": "${component.escape()}",
            "features": [
                ${features.joinToString(", ") { it.encodeToJson() }}
            ]
        }
    """.trimIndent()

internal fun FeatureToUpdateRequest.encodeToJson(): String = """
        {
            "name": "${name.escape()}",
            "description": "${description.escape()}",
            "specs": [
                ${specs.joinToString(", ") { it.encodeToJson() }}
            ]
        }
    """.trimIndent()

internal fun SpecificationToUpdateRequest.encodeToJson(): String = """
        {
            "team": "${team.escape()}",
            "name": "${name.escape()}",
            "status": "$status",
            "tags": [
                ${tags.joinToString(", ") { "\"${it.escape()}\"" }}
            ],
            "steps": [
                ${steps.joinToString(", ") { it.encodeToJson() }}
            ],
            "interactions": [
                ${interactions.joinToString(", ") { it.encodeToJson() }}
            ]
        }
    """.trimIndent()

internal fun SpecificationStep.encodeToJson(): String = """
    {
        "type": "$type",
        "description": "${description.escape()}",
        "index": $index
    }
""".trimIndent()

internal fun SpecInteraction.encodeToJson(): String = """
    {
        "direction": "$direction",
        "type": "$type",
        "name": "${name.escape()}",
        "metadata": {
            ${metadata.map { (key, value) -> """ "${key.escape()}": "${value.escape()}" """ }.joinToString(", ")}
        }
    }
""".trimIndent()
