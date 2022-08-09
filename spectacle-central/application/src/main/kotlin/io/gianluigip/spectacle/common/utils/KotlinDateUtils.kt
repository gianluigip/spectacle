package io.gianluigip.spectacle.common.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlinx.datetime.Instant as KInstant

fun ZonedDateTime.toKotlinInstant(): KInstant =
    KInstant.fromEpochMilliseconds(toUtcInstant().toEpochMilli())

fun KInstant.toLocalDateTime() = Instant.ofEpochMilli(toEpochMilliseconds()).atZone(ZoneId.of("UTC")).toLocalDateTime()