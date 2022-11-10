package io.gianluigip.spectacle.dsl.bdd

import io.gianluigip.spectacle.common.Features.SPECIFICATIONS
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Feature(SPECIFICATIONS)
@SpecTags(Tags.SPECIFICATIONS)
@ExtendWith(JUnitSpecificationReporter::class)
class BddSpecTest {

    @Test
    @Specification
    fun `The specification can be written in a BDD style`() {
        val spec = given("a behaviour") {
        } whenever "writing the test" then "you can use a BDD syntax" run {

            given("a first number") {
                2
            } and "a second number" run {
                2
            } whenever "adding both number" run {
                2 + 2
            } then "the result is 4" runAndFinish { result ->
                result shouldBe 4
            }

        }
        TestContext.setCurrentSpec(spec.specBuilder)
    }
}
