package io.gianluigip.spectacle.common.utils

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun Clock.toUtcLocalDateTime(): LocalDateTime = toZonedDateTime().withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()

fun Clock.toZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(instant(), zone)
}
