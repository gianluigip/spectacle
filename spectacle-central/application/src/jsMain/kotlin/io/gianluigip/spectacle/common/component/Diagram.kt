package io.gianluigip.spectacle.common.component

import io.gianluigip.spectacle.common.utils.lighter
import io.gianluigip.spectacle.home.ThemeContext
import kotlinx.browser.document
import mui.material.Box
import mui.material.styles.Theme
import react.FC
import react.Props
import react.useContext
import react.useEffect
import react.useState
import kotlin.random.Random

external interface DiagramProps : Props {
    var content: String
}

val Diagram = FC<DiagramProps> {
    val theme by useContext(ThemeContext)
    val diagramId by useState("diagram-${Random.nextInt()}")

    useEffect {
        document.getElementById(diagramId)?.removeAttribute("data-processed")
        mermaidInit()
    }

    Box {
        id = diagramId
        className = "mermaid"
        +"""
            ${generateMermaidTheme(theme)}
            ${it.content}
        """.trimIndent()
    }
}

private fun generateMermaidTheme(theme: Theme): String = """
    %%{init: {
        'theme': 'base',
        'themeVariables': {
            'darkMode': ${theme.palette.mode == "dark"},
            'primaryColor': '${theme.palette.info.main.lighter(80)}',
            'fontFamily': 'Roboto'
            }
        }
    }%%
""".trimIndent()