package io.gianluigip.spectacle.common.utils

import react.router.Search

fun Search.parseParams(): Map<String, String> {
    val params = mutableMapOf<String, String>()
    this.removePrefix("?").split("&").forEach {
        if (it.contains("=")) {
            val param = it.split("=")
            params[param.first()] = param.last().replaceEscapedSpaces()
        } else {
            params[it] = ""
        }
    }
    return params
}

fun String.replaceEscapedSpaces() = replace("%20", " ")
fun String.escapeSpaces() = replace(" ", "%20")
