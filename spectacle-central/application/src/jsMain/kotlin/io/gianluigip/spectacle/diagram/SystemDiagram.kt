package io.gianluigip.spectacle.diagram

import io.gianluigip.spectacle.common.component.Diagram
import mui.material.TextField
import mui.material.Typography
import react.FC
import react.Props
import react.dom.onChange
import react.useState

const val systemDiagramPath = "/system_diagram"
val SystemDiagram = FC<Props> {
    var testValue by useState("DSL")

    Typography {
        variant = "h3"
        +"System Diagram"
    }

    TextField {
        value = testValue
        onChange = { event -> testValue = event.target.asDynamic().value ?: "" }
    }

    Diagram {
        content = """
            graph LR
            sourceCode("Source Code")
            spectacleDsl("Spectacle $testValue")
            spectacleCentral("Spectacle Central")
            
            sourceCode --> spectacleDsl 
            spectacleDsl --> spectacleCentral 
        """.trimIndent()
    }
}
