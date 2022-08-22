package io.gianluigip.spectacle.common.utils

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class TestClock : Clock() {

    private var DEFAULT_TIME_ZONE = ZoneId.systemDefault()

    private var fixedTime = Instant.now()

    fun forwardMinutes(minutes: Int) {
        fixedTime = fixedTime.plus(minutes.toLong(), ChronoUnit.MINUTES)
    }

    override fun getZone(): ZoneId? {
        return DEFAULT_TIME_ZONE
    }

    override fun withZone(zone: ZoneId?): Clock? {
        throw UnsupportedOperationException()
    }

    override fun instant(): Instant {
        return fixedTime
    }
}
