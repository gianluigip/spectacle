package io.gianluigip.spectacle.specification.model

data class EventInteractionMetadata(
    val format: EventFormat,
    val schema: String,
    val dependencies: List<String>
) {

    companion object {
        fun isEventMetadataSimilar(interaction1: SpecInteraction, interaction2: SpecInteraction) =
            interaction1.metadata["format"] == interaction2.metadata["format"]

        fun fromMap(map: Map<String, String>) = EventInteractionMetadata(
            format = EventFormat.valueOf(map["format"]!!),
            schema = map["schema"] ?: "",
            dependencies = map.filterKeys { it.startsWith("dependency__") }.values.toList()
        )
    }

    fun toMap(): Map<String, String> = mutableMapOf(
        "metadataType" to "EVENT",
        "format" to format.name,
        "schema" to schema,
    ).also { map ->
        dependencies.forEachIndexed { index, dependency -> map["dependency__$index"] = dependency }
    }
}

fun SpecInteraction.hasEventMetadata() = metadata["metadataType"] == "EVENT" && metadata.contains("format") && metadata.contains("schema")

enum class EventFormat {
    PROTOBUF, JSON, AVRO
}
