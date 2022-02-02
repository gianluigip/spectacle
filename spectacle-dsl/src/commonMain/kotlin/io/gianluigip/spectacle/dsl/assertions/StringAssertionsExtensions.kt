package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.assertContains
import kotlin.test.assertTrue

private val Any?.expectedTo get() = "Expected '$this' to"

@AssertionDslMarker
infix fun String?.shouldStartWith(value: String?) =
    assertTrue(this?.startsWith(value ?: "") ?: false, "$expectedTo start with '$value'")

@AssertionDslMarker
infix fun String?.shouldEndWith(value: String?) =
    assertTrue(this?.endsWith(value ?: "") ?: false, "$expectedTo end with '$value'")

@AssertionDslMarker
infix fun String?.shouldContains(value: String) =
    assertContains(this ?: "", value, ignoreCase = false, "$expectedTo contains '$value'")

@AssertionDslMarker
fun String?.shouldNotBeEmpty() =
    assertTrue(this?.isNotEmpty() ?: false, "$expectedTo be not empty")

@AssertionDslMarker
fun String?.shouldBeEmpty() =
    assertTrue(this?.isEmpty() ?: false, "$expectedTo be empty")
