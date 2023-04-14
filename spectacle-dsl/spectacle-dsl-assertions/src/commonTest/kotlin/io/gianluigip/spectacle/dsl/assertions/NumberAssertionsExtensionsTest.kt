package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.Test

class NumberAssertionsExtensionsTest {

    @Test
    fun shouldBeCloseTo_should_assert_closer_values() {
        0.000001 shouldBeCloseTo 0.000002
    }

    @Test
    fun shouldBeCloseTo_should_fail_when_not_close_enough() =
        runAndCatch {
            0.001 shouldBeCloseTo 0.003
        }.message shouldStartWith "Expected '0.001' to be closer to '0.003' but the difference was bigger than 0.001"
}