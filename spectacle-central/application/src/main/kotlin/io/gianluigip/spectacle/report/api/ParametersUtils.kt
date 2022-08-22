package io.gianluigip.spectacle.report.api

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

fun <R> String?.splitAndMap(map: (String) -> R): Set<R>? = this?.split(",")?.map { map.invoke(it.trim()) }?.toSet()

fun String?.fromIsoInstant(): Instant? = this?.toInstant()
