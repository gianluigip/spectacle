package io.gianluigip.spectacle.common.components

import csstype.number
import js.core.jso
import mui.material.LinearProgress
import react.FC
import react.Props

external interface LoadingBarProps : Props {
    var isLoading: Boolean?
}

val LoadingBar = FC<LoadingBarProps> {
    if (it.isLoading == true) {
        LinearProgress { sx = jso { number(1.0) } }
    }
}
