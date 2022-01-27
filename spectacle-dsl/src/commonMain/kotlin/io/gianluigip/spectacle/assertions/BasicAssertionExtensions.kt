package io.gianluigip.spectacle.assertions

import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val Any?.expectedToBe get() = "Expected '$this' to be"

/**
 * Convenient method to define a block inside the object to assert.
 */
infix fun <T> T.assertThat(block: T.() -> Unit) = block.invoke(this)

infix fun <T> T.shouldBe(value: T) =
    assertEquals(this, value, "$expectedToBe equal to '$value'")

infix fun <T> T.shouldNotBe(value: T) =
    assertNotEquals(this, value, "$expectedToBe not equal to '$value'")

fun Any?.shouldBeNull() =
    assertNull(this, "$expectedToBe null")

fun Any?.shouldBeNotNull() =
    assertNotNull(this, "$expectedToBe not null")

infix fun Any.shouldBeInstanceOf(klass: KClass<*>) =
    assertTrue(klass.isInstance(this), "$expectedToBe instance of ${klass.qualifiedName}")
