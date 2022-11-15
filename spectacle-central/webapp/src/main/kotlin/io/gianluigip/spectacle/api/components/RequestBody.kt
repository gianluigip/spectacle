package io.gianluigip.spectacle.api.components

import io.gianluigip.spectacle.common.components.MetaDataChip
import io.gianluigip.spectacle.common.components.SectionTitle
import io.gianluigip.spectacle.wiki.components.MarkdownViewer
import mui.material.Grid
import mui.material.GridDirection
import mui.system.responsive
import react.FC
import react.Props

external interface RequestBodyProps : Props {
    var label: String
    var body: String?
    var contentType: String?
}

val RequestBody = FC<RequestBodyProps> { props ->

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
