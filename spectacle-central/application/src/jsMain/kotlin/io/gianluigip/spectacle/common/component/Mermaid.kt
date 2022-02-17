@file:JsModule("react-mermaid2")
@file:JsNonModule

package io.gianluigip.spectacle.common.component

import react.Props

/**
 * https://mermaid-js.github.io/mermaid/#/README
 */
@JsName("default")
external val Mermaid: react.FC<MermaidProps>

external interface MermaidConfig {
    var theme: String
}

external interface MermaidProps : Props {
    var chart: String
    var name: String
    var config: MermaidConfig
}