package io.gianluigip.spectacle.report.publisher

import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.StepType.AND
import io.gianluigip.spectacle.specification.model.StepType.AND_GIVEN
import io.gianluigip.spectacle.specification.model.StepType.AND_THEN
import io.gianluigip.spectacle.specification.model.StepType.AND_WHENEVER
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.StepType.THEN
import io.gianluigip.spectacle.specification.model.StepType.WHENEVER

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
