package io.gianluigip.spectacle.diagram

import io.gianluigip.spectacle.common.component.Mermaid
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.specification.component.FeaturesReportProps
import mui.material.TextField
import mui.material.Typography
import mui.material.styles.Theme
import react.FC
import react.dom.onChange
import react.useContext
import react.useState

const val systemDiagramPath = "/system_diagram"
val SystemDiagram = FC<FeaturesReportProps> {
    val theme by useContext(ThemeContext)
    var testValue by useState("DSL")

    Typography {
        variant = "h3"
        +"System Diagram"
    }

    TextField {
        value = testValue
        onChange = { event -> testValue = event.target.asDynamic().value ?: "" }
    }

    Mermaid {
        chart = """
            ${generateMermaidTheme(theme)}
            graph LR
            sourceCode("Source Code")
            spectacleDsl("Spectacle $testValue")
            spectacleCentral("Spectacle Central")
            
            sourceCode --> spectacleDsl 
            spectacleDsl --> spectacleCentral 
        """.trimIndent()
    }
}

private fun generateMermaidTheme(theme: Theme): String = """
    %%{init: {
        'theme': 'base',
        'themeVariables': {
            'primaryColor': '${theme.palette.info.main}',
            'fontFamily': 'Roboto'
            }
        }
    }%%
""".trimIndent()
