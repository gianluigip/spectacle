package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.Test

class ThrowableAssertionsExtensionsTest {

    @Test
    fun shouldBeCausedBy_should_assert_the_cause_of_a_throwable() {
        RuntimeException(IllegalArgumentException()) shouldBeCausedBy IllegalArgumentException::class
    }

    @Test
    fun shouldBeCausedBy_should_fail_if_the_cause_is_not_as_expected() =
        runAndCatch {
            RuntimeException(IllegalArgumentException()) shouldBeCausedBy IllegalStateException::class
        }.message shouldStartWith "Expected 'RuntimeException' to be caused by IllegalStateException"

}
