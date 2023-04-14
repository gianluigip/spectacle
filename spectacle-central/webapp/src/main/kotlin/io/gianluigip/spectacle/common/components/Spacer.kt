package io.gianluigip.spectacle.common.components

import csstype.Height
import csstype.Width
import js.core.jso
import mui.material.Box
import react.FC
import react.Props

external interface SpacerProps : Props {
    var width: Width?
    var height: Height?
}

val Spacer = FC<SpacerProps> {
    Box {
        sx = jso {
            it.width?.let { value -> width = value }
            it.height?.let { value -> height = value }
        }
    }
}
