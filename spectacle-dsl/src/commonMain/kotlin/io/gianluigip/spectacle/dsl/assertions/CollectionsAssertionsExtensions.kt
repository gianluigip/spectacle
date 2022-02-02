package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val Any?.expectedTo get() = "Expected '$this' to"

@AssertionDslMarker
infix fun Collection<*>?.shouldHasSize(expectedSize: Int) =
    assertEquals(this?.size, expectedSize, "$expectedTo has size $expectedSize")

@AssertionDslMarker
fun Collection<*>?.shouldBeEmpty() =
    assertTrue(this?.isEmpty() ?: false, "$expectedTo be empty")

@AssertionDslMarker
fun Collection<*>?.shouldNotBeEmpty() =
    assertTrue(this?.isNotEmpty() ?: false, "$expectedTo not be empty")

@AssertionDslMarker
infix fun <T> Collection<T>?.shouldContainsAll(elements: Collection<T>?) =
    assertTrue(this?.containsAll(elements ?: emptyList()) ?: false, "$expectedTo contains all elements in '$elements'")

@AssertionDslMarker
infix fun <T> Collection<T>?.shouldContains(element: T?) =
    assertTrue(this?.contains(element) ?: false, "$expectedTo contains '$element'")

@AssertionDslMarker
infix fun <T> Collection<T>?.shouldNotContains(element: T?) =
    assertTrue(!(this?.contains(element) ?: false), "$expectedTo not contains '$element'")
