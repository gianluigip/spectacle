package io.gianluigip.spectacle.api.components

import csstype.Color
import csstype.number
import io.gianluigip.spectacle.common.components.SectionTitle
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.EndpointRequestResponse
import js.core.jso
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.Stack
import mui.material.styles.Theme
import mui.system.responsive
import react.FC
import react.Props
import react.useRequiredContext

external interface EndpointRequestCardProps : Props {
    var request: EndpointRequestResponse
}

val EndpointRequestCard = FC<EndpointRequestCardProps> { props ->
    val theme by useRequiredContext(ThemeContext)
    val request = props.request

    Accordion {
        sx = jso { flexGrow = number(1.0) }

        AccordionSummary {
            sx = jso {
                flexGrow = number(1.0)
            }
            SectionTitle { text = request.responseStatus; color = getStatusColor(request.responseStatus, theme) }
        }

        AccordionDetails {

            Stack {
                spacing = responsive(2)

                RequestBody {
                    label = "Request"
                    body = request.requestBody
                    contentType = request.requestContentType
                }

                RequestBody {
                    label = "Response"
                    body = request.responseBody
                    contentType = request.responseContentType
                }
            }
        }
    }
}

private fun getStatusColor(status: String, theme: Theme): Color = when (status.firstOrNull()) {
    '2' -> theme.palette.success.main
    '4' -> theme.palette.warning.main
    '5' -> theme.palette.error.main
    else -> theme.palette.text.primary
}
