package io.gianluigip.spectacle.specification.model

import kotlinx.serialization.Serializable

@Serializable
data class SpecInteraction(
    val direction: InteractionDirection,
    val type: InteractionType,
    val name: String,
    val metadata: Map<String, String> = emptyMap(),
)

enum class InteractionDirection {
    INBOUND, OUTBOUND
}

enum class InteractionType {
    EVENT, HTTP, PERSISTENCE, LIBRARY
}
