package io.gianluigip.spectacle.dsl.bdd

import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.specification.model.SpecStatus
import kotlin.test.Test

class BddDslTest {

    @Test
    fun `Simplest example`() =
        given("a first number") {
            2
        } and "a second number" run {
            2
        } whenever "adding both number" run {
            2 + 2
        } then "the result is 4" runAndFinish { result ->
            result shouldBe 4
        }

    @Test
    fun `Full programmatic example`() = aSpec(
        specName = "A spec should support metadata info",
        featureName = "BDD DSL",
        featureDescription = "The DSL allow to write complex tests in a readable way",
        team = "Spectacle Team", status = SpecStatus.IMPLEMENTED,
        tags = mutableSetOf()
    ) given "some conditions" run {
    } andGiven "" run {
    } whenever "something happen" run {
    } andWhenever "" run {
    } then "the result is as expected" run {
    } andThen "" runAndFinish {
    }

}