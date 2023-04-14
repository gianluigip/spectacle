package io.gianluigip.spectacle.specification.components

import io.gianluigip.spectacle.common.components.label
import io.gianluigip.spectacle.common.components.onChange
import io.gianluigip.spectacle.common.components.renderInput
import io.gianluigip.spectacle.common.components.value
import io.gianluigip.spectacle.common.utils.parseISO
import io.gianluigip.spectacle.common.utils.toNode
import kotlinx.datetime.Instant
import mui.material.TextField
import muix.pickers.AdapterDateFns
import muix.pickers.DateTimePicker
import muix.pickers.LocalizationProvider
import react.FC
import react.Props
import react.create
import web.html.InputType

external interface ReportTimeFilterProps : Props {
    var label: String
    var value: Instant?
    var onFilterChanged: (Instant?) -> Unit
}

val ReportTimeFilter = FC<ReportTimeFilterProps> { props ->

    LocalizationProvider {
        dateAdapter = AdapterDateFns
        DateTimePicker {
            label = props.label
            value = props.value?.let { parseISO(it.toString()) }
            onChange = {
                props.onFilterChanged.invoke(
                    it?.let { Instant.parse(it.toISOString()) }
                )
            }
            renderInput = { params ->
                TextField.create {
                    +params
                    label = props.label.toNode()
                    type = InputType.search
                }
            }
        }
    }
}
