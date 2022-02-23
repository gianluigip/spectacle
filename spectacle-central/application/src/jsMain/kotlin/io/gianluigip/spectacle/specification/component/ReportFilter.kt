package io.gianluigip.spectacle.specification.component

import io.gianluigip.spectacle.common.utils.toNode
import kotlinx.js.jso
import mui.material.Autocomplete
import mui.material.AutocompleteProps
import mui.material.TextField
import react.FC
import react.Props
import react.create

external interface ReportFilterProps : Props {
    var label: String
    var value: String?
    var options: Collection<String>
    var onFilterChanged: (String?) -> Unit
}

private external interface SelectableOption {
    var label: String
}

private fun SelectableOption(label: String): SelectableOption = jso { this.label = label }

@Suppress("UPPER_BOUND_VIOLATED")
val ReportFilter = FC<ReportFilterProps> {

    Autocomplete<AutocompleteProps<SelectableOption>> {
        options = it.options.sorted().map { SelectableOption(it) }.toTypedArray()
        value = it.value?.let { value -> SelectableOption(value) }
        renderInput = { params ->
            TextField.create { +params;label = it.label.toNode() }
        }
        isOptionEqualToValue = { old, new -> old.label == new.label }
        onChange = { _, newValue: SelectableOption?, _, _ ->
            it.onFilterChanged.invoke(newValue?.label)
        }
    }
}
