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
    var isExpanded by useState(props.expanded ?: false)

    components.forEach { component ->
        Accordion {
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
                component.endpoints.sortedBy { "${it.path}-${it.method}" }.forEach {
                    ApiEndpointCard { endpoint = it }
                }
            }
        }
    }

}
