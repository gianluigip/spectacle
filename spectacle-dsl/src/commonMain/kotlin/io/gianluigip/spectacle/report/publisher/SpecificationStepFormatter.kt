package io.gianluigip.spectacle.report.publisher

import io.gianluigip.spectacle.specification.SpecificationStep
import io.gianluigip.spectacle.specification.StepType.AND
import io.gianluigip.spectacle.specification.StepType.AND_GIVEN
import io.gianluigip.spectacle.specification.StepType.AND_THEN
import io.gianluigip.spectacle.specification.StepType.AND_WHENEVER
import io.gianluigip.spectacle.specification.StepType.GIVEN
import io.gianluigip.spectacle.specification.StepType.THEN
import io.gianluigip.spectacle.specification.StepType.WHENEVER

object SpecificationStepFormatter {

    fun format(step: SpecificationStep): String {
        val stepTypeText = when (step.type) {
            GIVEN -> "Given"
            AND_GIVEN -> "And given"
            WHENEVER -> "Whenever"
            AND_WHENEVER -> "And whenever"
            THEN -> "Then"
            AND_THEN -> "And then"
            AND -> "And"
        }
        return "$stepTypeText ${step.description}"
    }
}
