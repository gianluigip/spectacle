package io.gianluigip.spectacle.diagram.components

import io.gianluigip.spectacle.common.components.mermaidInit
import io.gianluigip.spectacle.diagram.utils.generateMermaidConfigHeader
import io.gianluigip.spectacle.home.ThemeContext
import kotlinx.browser.document
import mui.material.Box
import react.FC
import react.Props
import react.useContext
import react.useEffect
import react.useState
import kotlin.random.Random

external interface DiagramProps : Props {
    var content: String
    var expandDiagram: Boolean?
}

val Diagram = FC<DiagramProps> {
    val theme by useContext(ThemeContext)
    val diagramId by useState("diagram-${Random.nextInt()}")

    useEffect {
        document.getElementById(diagramId)?.let { mermaidDiv ->
            if (!mermaidDiv.innerHTML.trim().startsWith("<svg")) {
                mermaidDiv.removeAttribute("data-processed")
                mermaidInit()
            }
        }
    }

    Box {
        id = diagramId
        className = "mermaid"
        +"""
            ${generateMermaidConfigHeader(theme, it.expandDiagram ?: false)}
            ${it.content}
        """.trimIndent()
    }
}