package io.gianluigip.spectacle.report.utils

fun String.removeStartAndEndSpacesOnEachLine() = lines().filter { it.isNotBlank() }.joinToString("\n") { it.trim() }
