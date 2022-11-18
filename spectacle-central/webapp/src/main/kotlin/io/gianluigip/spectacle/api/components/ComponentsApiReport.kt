package io.gianluigip.spectacle.api.components

import io.gianluigip.spectacle.common.components.SectionTitle
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.ComponentApiResponse
import kotlinx.js.jso
import mui.icons.material.ExpandMore
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import react.FC
import react.Props
import react.create
import react.useContext
import react.useState

external interface ComponentsApiReportProps : Props {
    var components: List<ComponentApiResponse>
    var expanded: Boolean?
}

val ComponentsApiReport = FC<ComponentsApiReportProps> { props ->
    val theme by useContext(ThemeContext)
    val components = props.components.sortedBy { it.component }

    components.forEach { component ->
        Accordion {
            var isExpanded by useState(props.expanded ?: false)
            expanded = isExpanded
            onChange = { _, expanded -> isExpanded = expanded }
            AccordionSummary {
                sx = jso {
                    color = theme.palette.info.contrastText
                    backgroundColor = theme.palette.info.main
                }
                expandIcon = ExpandMore.create()
                SectionTitle { text = component.component; color = theme.palette.info.contrastText }
            }
            AccordionDetails {
                component.endpoints.sortedBy { "${it.path}-${it.method.toMethodNumber()}" }.forEach {
                    ApiEndpointCard { endpoint = it }
                }
            }
        }
    }

}

private fun String.toMethodNumber() = when (uppercase()) {
    "GET" -> 1
    "PATCH" -> 2
    "OPTIONS" -> 3
    "PUT" -> 4
    "POST" -> 5
    "DELETE" -> 6
    else -> 99
}
