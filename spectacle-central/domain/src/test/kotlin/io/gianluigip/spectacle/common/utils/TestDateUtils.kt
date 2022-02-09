package io.gianluigip.spectacle.common.utils

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

val CLOCK = TestClock()
fun now() = CLOCK.toZonedDateTime()

fun ZonedDateTime.addMinutesAndTruncate(minutes: Long): ZonedDateTime {
    return this.plusMinutes(minutes).truncatedTo(ChronoUnit.SECONDS)
}
