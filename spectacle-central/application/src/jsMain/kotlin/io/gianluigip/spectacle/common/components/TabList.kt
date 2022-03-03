@file:JsModule("@mui/lab/TabList")
@file:JsNonModule

package io.gianluigip.spectacle.common.components

import react.Props

external interface TabListProps : Props {
    var onChange: (event: dynamic, newValue: String) -> Unit
}

@JsName("default")
external val TabList: react.FC<TabListProps>