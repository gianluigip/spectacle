package io.gianluigip.spectacle.api.components

import csstype.Color
import csstype.number
import csstype.px
import io.gianluigip.spectacle.common.components.MetaDataChip
import io.gianluigip.spectacle.common.components.SectionTitle
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.ComponentEndpointResponse
import io.gianluigip.spectacle.wiki.components.MarkdownViewer
import js.core.jso
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.Grid
import mui.material.GridDirection.Companion.row
import mui.material.styles.Theme
import mui.system.responsive
import react.FC
import react.Props
import react.useRequiredContext

external interface ApiEndpointCardProps : Props {
    var endpoint: ComponentEndpointResponse
}

val ApiEndpointCard = FC<ApiEndpointCardProps> { props ->
    val theme by useRequiredContext(ThemeContext)
    val endpoint = props.endpoint

    Accordion {
        sx = jso { flexGrow = number(1.0) }
        AccordionSummary {
            sx = jso {
                flexGrow = number(1.0)
            }
            Grid {
                container = true
                spacing = responsive(1)
                direction = responsive(row)

                Grid { item = true; SectionTitle { text = endpoint.method.uppercase();color = getMethodColor(endpoint.method, theme) } }
                Grid { item = true; SectionTitle { text = endpoint.path; color = theme.palette.text.primary } }
            }
        }

        AccordionDetails {
            Grid {
                container = true
                spacing = responsive(1)
                direction = responsive(row)

                if (endpoint.tags.isNotEmpty()) {
                    Grid { item = true; MetaDataChip { label = "Tags: ${endpoint.tags.sorted().joinToString(", ")}" } }
                }
                Grid { item = true; MetaDataChip { label = "Features: ${endpoint.features.sorted().joinToString(", ")}" } }
                Grid { item = true; MetaDataChip { label = "Teams: ${endpoint.teams.sorted().joinToString(", ")}" } }
                Grid { item = true; MetaDataChip { label = "Source: ${endpoint.sources.sorted().joinToString(", ")}" } }
            }
            Spacer { height = 5.px }

            if (endpoint.queryParameters.isNotEmpty()) {
                SectionTitle { text = "Query Parameters:" }
                MarkdownViewer {
                    content = endpoint.queryParameters.map { "* **${it.key}** - example `${it.value}`" }.joinToString("\n")
                }
            }

            SectionTitle { text = "Statuses:" }
            endpoint.requests.forEach { EndpointRequestCard { request = it } }
        }
    }
}

private fun getMethodColor(method: String, theme: Theme): Color = when (method.uppercase()) {
    "GET" -> theme.palette.info.main
    "PUT" -> theme.palette.warning.main
    "POST" -> theme.palette.success.main
    "DELETE" -> theme.palette.error.main
    else -> theme.palette.text.primary
}
