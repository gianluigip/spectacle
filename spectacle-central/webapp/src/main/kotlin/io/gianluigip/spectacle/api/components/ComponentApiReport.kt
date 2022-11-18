package io.gianluigip.spectacle.api.components

import io.gianluigip.spectacle.report.api.model.ComponentApiResponse
import react.FC
import react.Props

external interface ComponentApiReportProps : Props {
    var component: ComponentApiResponse
}

val ComponentApiReport = FC<ComponentApiReportProps> { props ->
    props.component.endpoints.sortedBy { "${it.path}-${it.method.toMethodNumber()}" }.forEach {
        ApiEndpointCard { endpoint = it }
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
