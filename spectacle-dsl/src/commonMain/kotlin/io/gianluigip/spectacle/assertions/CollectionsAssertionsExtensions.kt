package io.gianluigip.spectacle.assertions

import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val Any?.expectedTo get() = "Expected '$this' to"

infix fun Collection<*>?.shouldHasSize(expectedSize: Int) =
    assertEquals(this?.size, expectedSize, "$expectedTo has size $expectedSize")

fun Collection<*>?.shouldBeEmpty() =
    assertTrue(this?.isEmpty() ?: false, "$expectedTo be empty")

fun Collection<*>?.shouldNotBeEmpty() =
    assertTrue(this?.isNotEmpty() ?: false, "$expectedTo not be empty")

infix fun <T> Collection<T>?.shouldContainsAll(elements: Collection<T>?) =
    assertTrue(this?.containsAll(elements ?: emptyList()) ?: false, "$expectedTo contains all elements in '$elements'")

infix fun <T> Collection<T>?.shouldContains(element: T?) =
    assertTrue(this?.contains(element) ?: false, "$expectedTo contains '$element'")

infix fun <T> Collection<T>?.shouldNotContains(element: T?) =
    assertTrue(!(this?.contains(element) ?: false), "$expectedTo not contains '$element'")
