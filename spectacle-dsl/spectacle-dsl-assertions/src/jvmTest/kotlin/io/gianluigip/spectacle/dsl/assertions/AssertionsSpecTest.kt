package io.gianluigip.spectacle.dsl.assertions

import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Feature(Features.SPECIFICATIONS)
@SpecTags(Tags.SPECIFICATIONS)
@ExtendWith(JUnitSpecificationReporter::class)
class AssertionsSpecTest {

    private data class User(val name: String, val roles: List<String>)

    @Test
    @Specification
    fun `The specification can use a readable assertion DSL to validate the test`() =
        given("a test") {
        } whenever "validating the results" run {
        } then "you can use an assertion DSL to improve the readability" runAndFinish {

            val user = User(name = "John Doe", roles = listOf("Admin", "Sales"))
            user.shouldBeNotNull()

            user assertThat {
                name shouldStartWith "John"
                name shouldEndWith "Doe"
                roles shouldHasSize 2
                roles shouldContains "Admin"
            }
        }
}
