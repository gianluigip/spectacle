package io.gianluigip.spectacle.bdd

import io.gianluigip.spectacle.assertions.shouldBe
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