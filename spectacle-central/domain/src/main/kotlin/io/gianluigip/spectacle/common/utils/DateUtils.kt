package io.gianluigip.spectacle.common.utils

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun Clock.toUtcLocalDateTime(): LocalDateTime = toZonedDateTime().withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()

fun Clock.toZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(instant(), zone)
}

fun LocalDateTime.fromUtc(clock: Clock): ZonedDateTime = atZone(ZoneId.of("UTC")).withZoneSameInstant(clock.zone)

fun ZonedDateTime.toUtcInstant(): Instant = withZoneSameInstant(ZoneOffset.UTC).toInstant()
