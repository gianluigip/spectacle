package io.gianluigip.spectacle.feature.components

import csstype.Color
import csstype.Display
import csstype.FlexShrink
import csstype.px
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.feature.api.model.FeatureResponse
import io.gianluigip.spectacle.home.ThemeContext
import kotlinx.datetime.Clock
import kotlinx.js.jso
import mui.material.Autocomplete
import mui.material.AutocompleteProps
import mui.material.Box
import mui.material.TextField
import mui.material.Typography
import react.FC
import react.Props
import react.create
import react.useContext

external interface FeaturesSelectorProps : Props {
    var features: List<FeatureResponse>
    var selected: FeatureResponse?
    var onSelect: (FeatureResponse?) -> Unit
}

@Suppress("UPPER_BOUND_VIOLATED")
val FeaturesSelector = FC<FeaturesSelectorProps> { props ->
    val theme by useContext(ThemeContext)
    val features = props.features

    Autocomplete<AutocompleteProps<FeatureResponse>> {
        options = features.sortedBy { it.name }.toTypedArray()
        value = props.selected ?: emptyFeature()
        renderInput = { params -> TextField.create { +params; label = "Features".toNode() } }
        isOptionEqualToValue = { old, new -> old.name == new.name }
        getOptionLabel = { option -> option.name }
        renderOption = { props, option, state ->
            Box.create {
                +props
                sx = jso { component = "li".asDynamic() }

                Box {
                    sx = jso { display = Display.flex }

                    Typography {
                        sx = jso { flexShrink = FlexShrink(0.0) }
                        +option.name
                    }
                    if (option.description.isNotEmpty()) {
                        Spacer { width = 5.px }
                        Typography {
                            sx = jso { color = Color(theme.palette.text.secondary) }
                            noWrap = true
                            +"- ${option.description}"
                        }
                    }
                }
            }
        }
        onChange = { _, newValue: FeatureResponse?, _, _ ->
            props.onSelect.invoke(newValue)
        }
    }
}

private fun emptyFeature() = FeatureResponse("", "", emptyList(), emptyList(), Clock.System.now(), Clock.System.now())
