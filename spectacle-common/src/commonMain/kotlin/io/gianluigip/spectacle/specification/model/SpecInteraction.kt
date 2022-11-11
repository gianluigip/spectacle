package io.gianluigip.spectacle.specification.model

import io.gianluigip.spectacle.specification.model.HttpInteractionMetadata.Companion.isHttpMetadataSimilar
import kotlinx.serialization.Serializable

@Serializable
data class SpecInteraction(
    val direction: InteractionDirection,
    val type: InteractionType,
    val name: String,
    val metadata: Map<String, String> = emptyMap(),
) {
    fun isSimilar(interaction: SpecInteraction) =
        direction == interaction.direction && type == interaction.type && name == interaction.name && when {
            hasHttpMetadata() && interaction.hasHttpMetadata() -> isHttpMetadataSimilar(this, interaction)
            else -> metadata == interaction.metadata
        }


}

enum class InteractionDirection {
    INBOUND, OUTBOUND
}

enum class InteractionType {
    EVENT, HTTP, PERSISTENCE, LIBRARY
}
