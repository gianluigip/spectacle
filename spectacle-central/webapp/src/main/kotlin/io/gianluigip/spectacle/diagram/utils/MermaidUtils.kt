package io.gianluigip.spectacle.diagram.utils

import io.gianluigip.spectacle.common.utils.lighter
import mui.material.styles.Theme

fun generateMermaidConfigHeader(theme: Theme, expandDiagram: Boolean = false): String = """
    %%{init: {
        'theme': 'base',
        'themeVariables': {
            'darkMode': ${theme.palette.mode == "dark"},
            'primaryColor': '${theme.palette.info.main.lighter(80)}',
            'fontFamily': 'Roboto'
            },
        'flowchart': {
            'useMaxWidth': ${!expandDiagram}
            }            
        }
    }%%
""".trimIndent()