package io.gianluigip.spectacle.component.components

import csstype.Display
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.component.api.Component
import io.gianluigip.spectacle.feature.api.model.FeatureResponse
import js.core.jso
import kotlinx.datetime.Clock
import mui.material.*
import react.FC
import react.Props
import react.create

external interface ComponentSelectorProps : Props {
    var components: List<Component>
    var selected: Component?
    var onSelect: (Component?) -> Unit
}

@Suppress("UPPER_BOUND_VIOLATED")
val ComponentSelector = FC<ComponentSelectorProps> { props ->
    val components = props.components

    Autocomplete<AutocompleteProps<Component>> {
        options = components.sortedBy { it.name }.toTypedArray()
        value = props.selected ?: emptyFeature()
        renderInput = { params -> TextField.create { +params; label = "Components".toNode() } }
        isOptionEqualToValue = { old, new -> old.name == new.name }
        getOptionLabel = { option -> option.name }
        renderOption = { props, option, state ->
            Box.create {
                +props
                sx = jso { display = "li".asDynamic() }

                Box {
                    sx = jso { display = Display.flex }

                    Typography { +option.name }
                }
            }
        }
        onChange = { _, newValue: Component?, _, _ ->
            props.onSelect.invoke(newValue)
        }
    }
}

private fun emptyFeature() = FeatureResponse("", "", emptyList(), emptyList(), Clock.System.now(), Clock.System.now())
