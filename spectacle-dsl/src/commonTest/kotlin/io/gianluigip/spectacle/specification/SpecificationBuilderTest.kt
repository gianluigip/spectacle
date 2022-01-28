package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.assertions.assertThat
import io.gianluigip.spectacle.assertions.shouldBe
import io.gianluigip.spectacle.specification.StepType.GIVEN
import io.gianluigip.spectacle.specification.StepType.THEN
import io.gianluigip.spectacle.specification.StepType.WHENEVER
import kotlin.test.Test

class SpecificationBuilderTest {

    @Test
    fun `Should build a specification`() {
        val specBuilder = SpecificationBuilder().apply {
            specName = "Spec 1"
            featureName = "Feature 1"
            featureDescription = "Short Description"
            team = "Spectacle Team"
            status = SpecStatus.PARTIALLY_IMPLEMENTED
            tags = mutableSetOf("Tag1", "Tag2")

            addStep(GIVEN, "First step")
            addStep(WHENEVER, "Second step")
            addStep(THEN, "Third step")
        }

        specBuilder.build() assertThat {
            name shouldBe "Spec 1"
            metadata assertThat {
                featureName shouldBe "Feature 1"
                featureDescription shouldBe "Short Description"
                team shouldBe "Spectacle Team"
                status shouldBe SpecStatus.PARTIALLY_IMPLEMENTED
                tags shouldBe listOf("Tag1", "Tag2")
            }
            steps shouldBe listOf(
                SpecificationStep(GIVEN, "First step", 1),
                SpecificationStep(WHENEVER, "Second step", 2),
                SpecificationStep(THEN, "Third step", 3)
            )
        }
    }

    @Test
    fun `Should work properly with only default values`() {
        SpecificationBuilder().build() assertThat {
            name shouldBe "Unknown"
            metadata assertThat {
                featureName shouldBe "Unknown"
                featureDescription shouldBe ""
                team shouldBe "Unknown"
                status shouldBe SpecStatus.IMPLEMENTED
                tags shouldBe listOf()
            }
            steps shouldBe listOf()
        }
    }
}