package io.gianluigip.spectacle.common.component

import csstype.FlexGrow
import kotlinext.js.jso
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