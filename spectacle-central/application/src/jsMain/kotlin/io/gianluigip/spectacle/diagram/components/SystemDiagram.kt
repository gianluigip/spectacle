package io.gianluigip.spectacle.diagram.components

import io.gianluigip.spectacle.common.component.Diagram
import io.gianluigip.spectacle.report.api.model.SystemInteractionResponse
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.InteractionType.PERSISTENCE
import react.FC
import react.Props

external interface SystemDiagramProps : Props {
    var interactions: List<SystemInteractionResponse>
}

val SystemDiagram = FC<SystemDiagramProps> {

    Diagram {
        content = """
            graph LR
            ${generateComponents(it.interactions)} 
            ${generateDiagramInteractions(it.interactions)}
        """.trimIndent()
    }
}

private fun generateComponents(interactions: List<SystemInteractionResponse>): String {
    val components = mutableSetOf<String>()
    interactions.forEach { interaction ->
        components += generateElementShape(interaction.component, HTTP)
        components += generateElementShape(interaction.interactionName, interaction.type)
    }
    return components.joinToString("\n")
}

private fun generateElementShape(name: String, type: InteractionType): String {
    val elementId = name.toDiagramId()
    return when (type) {
        EVENT -> """$elementId{{"$name"}}"""
        PERSISTENCE -> """$elementId[("$name")]"""
        HTTP -> """$elementId(["$name"])"""
        else -> ""
    }
}

private fun generateDiagramInteractions(interactions: List<SystemInteractionResponse>): String {
    val mermaidInteractions = mutableSetOf<String>()
    interactions.forEach {
        val mermaidInteraction = when (it.direction) {
            InteractionDirection.INBOUND -> "${it.interactionName.toDiagramId()} --> ${it.component.toDiagramId()}"
            InteractionDirection.OUTBOUND -> "${it.component.toDiagramId()} --> ${it.interactionName.toDiagramId()}"
            else -> ""
        }
        mermaidInteractions += mermaidInteraction
    }
    return mermaidInteractions.joinToString("\n")
}

private fun String.toDiagramId() = replace(" ", "")
