package io.gianluigip.spectacle.api.components

import csstype.number
import io.gianluigip.spectacle.common.components.MetaDataChip
import io.gianluigip.spectacle.common.components.SectionTitle
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.report.api.model.EndpointRequestResponse
import io.gianluigip.spectacle.wiki.components.MarkdownViewer
import kotlinx.js.jso
import mui.icons.material.Subtitles
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Stack
import mui.material.Typography
import mui.system.responsive
import react.FC
import react.Props
import react.useContext

external interface EndpointRequestCardProps : Props {
    var request: EndpointRequestResponse
}

val EndpointRequestCard = FC<EndpointRequestCardProps> { props ->
    val theme by useContext(ThemeContext)
    val request = props.request

    Accordion {
        sx = jso { flexGrow = number(1.0) }

        AccordionSummary {
            sx = jso {
                flexGrow = number(1.0)
            }
            SectionTitle { text = request.responseStatus; color = theme.palette.text.primary }
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

private external interface RequestBodyProps : Props {
    var label: String
    var body: String?
    var contentType: String?
}

private val RequestBody = FC<RequestBodyProps> { props ->
    val theme by useContext(ThemeContext)

    props.body?.let { body ->
        SectionTitle { text = "${props.label}:" }
        if (props.contentType != null) {
            Grid {
                container = true
                spacing = responsive(1)
                direction = responsive(GridDirection.row)

                MetaDataChip { label = "Content Type: ${props.contentType}" }
            }
        }
        MarkdownViewer {
            content = "```json\n${body.toPrettyJson()}\n```"
        }
    }
}

private fun String.toPrettyJson(): String {
    if (isEmpty()) {
        return "No Content"
    }
    val json = try {
        JSON.parse(this)
    } catch (ex: Throwable) {
        println("Failed to parse json '$this', error: ${ex.message}")
        "Error parsing"
    }
    return JSON.stringify(json, null, 2)
}
