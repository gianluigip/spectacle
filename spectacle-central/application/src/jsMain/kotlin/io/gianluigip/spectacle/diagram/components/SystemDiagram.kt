package io.gianluigip.spectacle.diagram.components

import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.navigation.generateSpecificationReportExternalLink
import io.gianluigip.spectacle.report.api.model.SystemInteractionResponse
import io.gianluigip.spectacle.specification.component.FiltersSelected
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.InteractionType.PERSISTENCE
import mui.material.FormControlLabel
import mui.material.FormGroup
import mui.material.Switch
import react.FC
import react.Props
import react.create
import react.useState

external interface SystemDiagramProps : Props {
    var interactions: List<SystemInteractionResponse>
    var components: Set<String>
}

val SystemDiagram = FC<SystemDiagramProps> {

    var expandDiagram by useState(false)
    FormGroup {
        FormControlLabel {
            label = "Expand Diagram".toNode()
            control = Switch.create {
                checked = expandDiagram
                onChange = { _, newValue -> expandDiagram = newValue }
            }
        }
    }

    Diagram {
        content = """
            graph LR
            ${generateComponents(it.components)} 
            ${generateComponents(it.interactions)} 
            ${generateDiagramInteractions(it.interactions)}
            ${generateComponentsLink(it.interactions)}
        """.trimIndent()
        this.expandDiagram = expandDiagram
    }
}

private fun generateComponents(components: Set<String>): String =
    components.joinToString("\n") { component ->
        generateElementShape(component, HTTP)
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

private fun generateComponentsLink(interactions: List<SystemInteractionResponse>): String {
    val links = mutableSetOf<String>()
    interactions.forEach { interaction ->
        links += generateComponentLink(interaction.component)
    }
    return links.joinToString("\n")
}

private fun generateComponentLink(component: String): String =
    """click ${component.toDiagramId()} "${generateSpecificationReportExternalLink(FiltersSelected(component = component))}" _blank"""

private fun generateDiagramInteractions(interactions: List<SystemInteractionResponse>): String {
    val mermaidInteractions = mutableSetOf<String>()
    interactions.forEach {
        val componentId = it.component.toDiagramId()
        val interactionId = it.interactionName.toDiagramId()
        val mermaidInteraction = when (it.direction) {
            INBOUND -> generateInboundInteractions(componentId, interactionId, it.type)
            OUTBOUND -> generateOutboundInteractions(componentId, interactionId, it.type)
            else -> ""
        }
        mermaidInteractions += mermaidInteraction
    }
    return mermaidInteractions.joinToString("\n")
}

private fun generateInboundInteractions(componentId: String, interactionId: String, type: InteractionType): String = when (type) {
    EVENT -> "$interactionId -- Consume --> $componentId"
    HTTP -> "$interactionId -- Request --> $componentId"
    else -> "$interactionId --> $componentId"
}

private fun generateOutboundInteractions(componentId: String, interactionId: String, type: InteractionType): String = when (type) {
    EVENT -> "$componentId -- Produce --> $interactionId"
    HTTP -> "$componentId -- Request --> $interactionId"
    else -> "$componentId -- Connect --> $interactionId"
}

private fun String.toDiagramId() = replace(" ", "")
