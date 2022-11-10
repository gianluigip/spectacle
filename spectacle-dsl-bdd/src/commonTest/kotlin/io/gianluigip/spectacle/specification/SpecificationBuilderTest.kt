package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.StepType.THEN
import io.gianluigip.spectacle.specification.model.StepType.WHENEVER
import kotlin.test.Test

class SpecificationBuilderTest {

    @Test
    fun should_build_a_specification() {
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
            addInteraction(SpecInteraction(INBOUND, EVENT, "Int1", mutableMapOf("m1" to "v1")))
            addInteraction(SpecInteraction(InteractionDirection.OUTBOUND, InteractionType.HTTP, "Int2", emptyMap()))
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
            interactions shouldBe listOf(
                SpecInteraction(INBOUND, EVENT, "Int1", mutableMapOf("m1" to "v1")),
                SpecInteraction(InteractionDirection.OUTBOUND, InteractionType.HTTP, "Int2", emptyMap())
            )
        }
    }

    @Test
    fun should_work_properly_with_only_default_values() {
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
            interactions shouldBe listOf()
        }
    }
}
