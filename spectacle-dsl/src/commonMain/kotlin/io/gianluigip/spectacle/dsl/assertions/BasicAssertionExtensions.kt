package io.gianluigip.spectacle.dsl.assertions

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
@AssertionDslMarker
infix fun <T> T.assertThat(block: T.() -> Unit) = block.invoke(this)

@AssertionDslMarker
infix fun <T> T.shouldBe(value: T) =
    assertEquals(this, value, "$expectedToBe equal to '$value'")

@AssertionDslMarker
infix fun <T> T.shouldNotBe(value: T) =
    assertNotEquals(this, value, "$expectedToBe not equal to '$value'")

@AssertionDslMarker
fun Any?.shouldBeNull() =
    assertNull(this, "$expectedToBe null")

@AssertionDslMarker
fun Any?.shouldBeNotNull() =
    assertNotNull(this, "$expectedToBe not null")

@AssertionDslMarker
infix fun Any.shouldBeInstanceOf(klass: KClass<*>) =
    assertTrue(klass.isInstance(this), "$expectedToBe instance of ${klass.qualifiedName}")
