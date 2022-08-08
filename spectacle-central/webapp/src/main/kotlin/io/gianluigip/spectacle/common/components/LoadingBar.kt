package io.gianluigip.spectacle.common.components

import csstype.FlexGrow
import kotlinx.js.jso
import mui.material.LinearProgress
import react.FC
import react.Props

external interface LoadingBarProps : Props {
    var isLoading: Boolean?
}

val LoadingBar = FC<LoadingBarProps> {
    if (it.isLoading == true) {
        LinearProgress { sx = jso { FlexGrow(1.0) } }
    }
}