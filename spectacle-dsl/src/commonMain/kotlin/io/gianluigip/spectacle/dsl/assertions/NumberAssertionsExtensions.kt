package io.gianluigip.spectacle.dsl.assertions

import kotlin.math.abs
import kotlin.test.assertTrue

private val Any?.expectedTo get() = "Expected '$this' to"

@AssertionDslMarker
infix fun Double?.shouldBeCloseTo(value: Double) = shouldBeCloseTo(value, 0.001)

@AssertionDslMarker
fun Double?.shouldBeCloseTo(value: Double, delta: Double) {
    val difference = abs((this ?: 0.0) - value)
    assertTrue(difference <= delta, "$expectedTo be closer to '$value' but the difference was bigger than $delta")
}
