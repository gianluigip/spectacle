package io.gianluigip.spectacle.report.publisher

import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.specification.Specification
import io.gianluigip.spectacle.specification.SpecificationMetadata
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.StepType.THEN
import io.gianluigip.spectacle.specification.model.StepType.WHENEVER
import kotlin.test.Test
import io.gianluigip.spectacle.specification.model.SpecificationStep as Step

class TerminalPublisherTest {

    @Test
    fun `should_generate_the_report_content`() {
        val spec1 = Specification(
            name = "Spec1",
            metadata = SpecificationMetadata(
                featureName = "Feature1", featureDescription = "\nDescription1\n\t\tMulti line\n", team = "Team1",
                status = SpecStatus.IMPLEMENTED, tags = listOf()
            ),
            steps = mutableListOf(
                Step(THEN, "Step2", 1),
                Step(WHENEVER, "Step1", 0)
            ),
            interactions = emptyList(),
        )
        val spec2 = Specification(
            name = "Spec2",
            metadata = SpecificationMetadata(
                featureName = "Feature1", featureDescription = "", team = "Team1", status = SpecStatus.NOT_IMPLEMENTED, tags = listOf()
            ),
            steps = mutableListOf(Step(GIVEN, "Step2", 0)),
            interactions = emptyList(),
        )
        val spec3 = Specification(
            name = "Spec3",
            metadata = SpecificationMetadata(
                featureName = "Feature2", featureDescription = "", team = "Team1", status = SpecStatus.PARTIALLY_IMPLEMENTED, tags = listOf()
            ),
            steps = mutableListOf(Step(GIVEN, "Step3", 0)),
            interactions = emptyList(),
        )
        val specs = listOf(spec2, spec1, spec3)

        val reportContent = TerminalPublisher.generateReportContent(specs)
        reportContent shouldBe "\nPublishing Specifications:\n" +
                "\n" +
                "Feature: Feature1\n" +
                "\n" +
                "\tSpec1\n" +
                "\t\tWhenever Step1\n" +
                "\t\tThen Step2\n" +
                "\n" +
                "\tSpec2 (Not Implemented)\n" +
                "\t\tGiven Step2\n" +
                "\n" +
                "\n" +
                "Feature: Feature2\n" +
                "\n" +
                "\tSpec3 (Partially Implemented)\n" +
                "\t\tGiven Step3\n" +
                "\n"
    }
}