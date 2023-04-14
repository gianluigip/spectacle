package io.gianluigip.spectacle.api.components

import io.gianluigip.spectacle.common.components.SectionTitle
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.ComponentApiResponse
import js.core.jso
import mui.icons.material.ExpandMore
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import react.*

external interface ComponentsApiReportProps : Props {
    var components: List<ComponentApiResponse>
    var expanded: Boolean?
}

val ComponentsApiReport = FC<ComponentsApiReportProps> { props ->
    val theme by useRequiredContext(ThemeContext)
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
                ComponentApiReport { this.component = component }
            }
        }
    }

}
