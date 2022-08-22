package io.gianluigip.spectacle.common.components

import io.gianluigip.spectacle.common.utils.toNode
import mui.material.TextField
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.onChange

external interface SearchTextFieldProps : Props {
    var label: String
    var value: String
    var onChange: (String?) -> Unit
}

val SearchTextField = FC<SearchTextFieldProps> { props ->
    TextField {
        label = props.label.toNode()
        value = props.value
        type = InputType.search
        onChange = { newValue -> props.onChange.invoke(newValue.target.asDynamic().value as String) }
    }
}
