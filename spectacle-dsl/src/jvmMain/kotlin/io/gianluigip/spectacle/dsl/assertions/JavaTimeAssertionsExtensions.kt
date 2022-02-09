package io.gianluigip.spectacle.dsl.assertions

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.test.assertTrue

private const val DEFAULT_OFFSET = 100L
private val Any?.expectedTo get() = "Expected '$this' to"

@AssertionDslMarker
infix fun ZonedDateTime?.shouldBeCloseTo(time: ZonedDateTime) = shouldBeCloseTo(time, offsetInMillis = DEFAULT_OFFSET)

@AssertionDslMarker
fun ZonedDateTime?.shouldBeCloseTo(time: ZonedDateTime, offsetInMillis: Long = DEFAULT_OFFSET) =
    this?.withZoneSameInstant(ZoneOffset.UTC)?.toInstant().shouldBeCloseTo(
        time = time.withZoneSameInstant(ZoneOffset.UTC).toInstant(),
        offsetInMillis = offsetInMillis,
    )

@AssertionDslMarker
infix fun Instant?.shouldBeCloseTo(time: Instant) = shouldBeCloseTo(time, offsetInMillis = DEFAULT_OFFSET)

@AssertionDslMarker
fun Instant?.shouldBeCloseTo(time: Instant, offsetInMillis: Long = DEFAULT_OFFSET) {
    val epochTime = this?.toEpochMilli() ?: 0
    val difference = abs(epochTime - time.toEpochMilli())
    assertTrue(difference <= offsetInMillis, "$expectedTo be closer to '$time' but the difference was bigger than $offsetInMillis milli seconds")
}

