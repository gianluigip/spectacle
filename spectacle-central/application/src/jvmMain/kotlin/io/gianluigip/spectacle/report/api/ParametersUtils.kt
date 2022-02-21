package io.gianluigip.spectacle.report.api

fun <R> String?.splitAndMap(map: (String) -> R): Set<R>? = this?.split(",")?.map { map.invoke(it.trim()) }?.toSet()