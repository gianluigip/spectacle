package io.gianluigip.spectacle.common.components

import io.gianluigip.spectacle.common.utils.toNode
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.material.TextField
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.onChange
import react.useEffectOnce
import react.useState
import kotlin.random.Random

private const val DEFAULT_DELAY = 500L
private val valueIds = mutableMapOf<Long, Long>()

external interface SearchTextFieldProps : Props {
    var label: String
    var value: String
    var onChange: (String?) -> Unit
}

val SearchTextField = FC<SearchTextFieldProps> { props ->

    val searchFieldId by useState(Random.nextLong())
    var searchValue by useState(props.value)

    useEffectOnce { valueIds.clear() }

    fun onChange(newValue: String) {
        searchValue = newValue
        val newValueId = Random.nextLong()
        valueIds[searchFieldId] = newValueId
        if (newValue.isEmpty()) {
            props.onChange(newValue)
        } else {
            MainScope().launch {
                delay(DEFAULT_DELAY)
                if (newValueId == valueIds[searchFieldId]) {
                    props.onChange(newValue)
                }
            }
        }
    }

    TextField {
        label = props.label.toNode()
        value = searchValue
        type = InputType.search
        onChange = { newValue -> onChange(newValue.target.asDynamic().value as String) }
    }
}
