package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.Test

class NumberAssertionsExtensionsTest {

    @Test
    fun `shouldBeCloseTo should assert closer values`() {
        0.000001 shouldBeCloseTo 0.000002
    }

    @Test
    fun `shouldBeCloseTo should fail when not close enough`() =
        runAndCatch {
            0.001 shouldBeCloseTo 0.003
        }.message shouldStartWith "Expected '0.001' to be closer to '0.003' but the difference was bigger than 0.001"
}