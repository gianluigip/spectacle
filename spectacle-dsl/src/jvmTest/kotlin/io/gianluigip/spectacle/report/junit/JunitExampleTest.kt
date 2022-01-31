package io.gianluigip.spectacle.report.junit

import io.gianluigip.spectacle.bdd.annotations.Feature
import io.gianluigip.spectacle.bdd.annotations.NotImplemented
import io.gianluigip.spectacle.bdd.annotations.PartiallyImplemented
import io.gianluigip.spectacle.bdd.annotations.SpecTags
import io.gianluigip.spectacle.bdd.annotations.Specification
import io.gianluigip.spectacle.bdd.annotations.Team
import io.gianluigip.spectacle.bdd.given
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Team("Matching")
@Feature(
    "First Feature", description = """
    Description defining the first feature.
    Using multiple lines and annotations.
"""
)
@Tags(Tag("Tag1"), Tag("Tag2"))
@SpecTags("Tag3", "Tag4")
@ExtendWith(JUnitSpecificationReporter::class)
class JunitExampleTest {

    @Test
    @Specification
    @Tags(Tag("Tag6"))
    @SpecTags("Tag5")
    @PartiallyImplemented
    fun `First test to execute`() =
        given("a test with multiple steps") {
        } whenever "it executes" run {
        } then "it should register all the BDD steps" runAndFinish {
        }

    @Test
    @Specification
    @NotImplemented
    fun `First feature second case to execute`() =
        given("a test with multiple steps") {
        } whenever "it executes" run {
        } then "it should register all the BDD steps" runAndFinish {
        }

    @Test
    fun `This should not be included because doesn't have the annotation`() =
        given("a test with multiple steps") {
        } whenever "it executes" run {
        } then "it should register all the BDD steps" runAndFinish {
        }
}