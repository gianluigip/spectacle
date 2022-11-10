package io.gianluigip.spectacle.dsl.assertions

import kotlin.reflect.KClass
import kotlin.test.assertTrue

private val Throwable.expectedTo get() = "Expected '${this::class.simpleName}' to"

@AssertionDslMarker
infix fun Throwable.shouldBeCausedBy(expected: KClass<*>) =
    assertTrue(expected.isInstance(this.cause), "$expectedTo be caused by ${expected.simpleName}")