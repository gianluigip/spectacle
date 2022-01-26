package com.gianluigip.spectacle.bdd

import com.gianluigip.spectacle.assertions.shouldBe
import kotlin.test.Test

class BddDslTest {

    @Test
    fun `BDD DSL example addition`() =
        given("a first number") {
            2
        } and "a second number" execute {
            2
        } whenever "adding both number" execute {
            2 + 2
        } then "the result is 4" validate { result ->
            result shouldBe 4
        }

}