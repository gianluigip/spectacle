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
    var label: String?
    var body: String?
    var contentType: String?
    var bodyType: String?
}

val RequestBody = FC<RequestBodyProps> { props ->

    props.body?.let { body ->
        props.label?.let { SectionTitle { text = "${props.label}:" } }
        if (props.contentType != null) {
            Grid {
                container = true
                spacing = responsive(1)
                direction = responsive(GridDirection.row)

                MetaDataChip { label = "Content Type: ${props.contentType}" }
            }
        }
        val bodyType = markdownType(bodyType = props.bodyType, contentType = props.contentType, body = props.body)
        MarkdownViewer {
            content = "```json\n${body.toPrettyPrint(bodyType)}\n```"
        }
    }
}

private fun markdownType(bodyType: String?, contentType: String?, body: String?): String = when {
    bodyType != null -> bodyType
    contentType != null -> when {
        contentType.contains("json") -> "json"
        contentType.contains("xml") -> "xml"
        else -> "json"
    }
    else -> "json"
}

private fun String.toPrettyPrint(markdownType: String): String {
    if (isEmpty()) {
        return "No Content"
    }
    return when (markdownType) {
        "json" -> toPrettyJson()
        else -> this
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
        this
    }
    return JSON.stringify(json, null, 2)
}
