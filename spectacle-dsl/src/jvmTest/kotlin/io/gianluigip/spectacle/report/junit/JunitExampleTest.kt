package io.gianluigip.spectacle.report.junit

import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.NotImplemented
import io.gianluigip.spectacle.dsl.bdd.annotations.PartiallyImplemented
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.annotations.Team
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.dsl.bdd.whenever
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test

@Team("Spectacle Team")
@Feature(
    "First Feature", description = """
    Description defining the first feature.
    Using multiple lines and annotations.
"""
)
@Tags(Tag("Tag1"), Tag("Tag2"))
@SpecTags("Tag3", "Tag4")
//@ExtendWith(JUnitSpecificationReporter::class)
class JunitExampleTest {

    @Test
    @Specification
    @Tags(Tag("Tag6"))
    @SpecTags("Tag5")
    @PartiallyImplemented
    fun `First feature first spec to execute`() =
        given("a test with multiple steps") {
        } whenever "it executes" run {
        } then "it should register all the BDD steps" runAndFinish {
        }

    @Test
    @Specification
    @NotImplemented
    fun `First feature second spec to execute`() =
        whenever("it executes") {
        } then "it should register all the BDD steps" runAndFinish {
        }

    @Test
    fun `This should not be included because doesn't have the annotation`() =
        given("a test with multiple steps") {
        } whenever "it executes" run {
        } then "it should register all the BDD steps" runAndFinish {
        }
}