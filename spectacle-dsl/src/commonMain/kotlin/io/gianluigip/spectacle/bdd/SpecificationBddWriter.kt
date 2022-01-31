package io.gianluigip.spectacle.bdd

import io.gianluigip.spectacle.specification.SpecificationBuilder
import io.gianluigip.spectacle.specification.StepType

/**
 * Provide a DSL for writing specifications as readable as possible.
 */
class SpecificationBddWriter<T>(
    val specBuilder: SpecificationBuilder,
    private val stepLastValue: T,
) {

    infix fun given(description: String) = addStep(StepType.GIVEN, description)

    infix fun andGiven(description: String) = addStep(StepType.AND_GIVEN, description)

    infix fun whenever(description: String) = addStep(StepType.WHENEVER, description)

    infix fun andWhenever(description: String) = addStep(StepType.AND_WHENEVER, description)

    infix fun then(description: String) = addStep(StepType.THEN, description)

    infix fun andThen(description: String) = addStep(StepType.AND_THEN, description)

    infix fun and(description: String) = addStep(StepType.AND, description)

    private fun addStep(type: StepType, description: String): SpecificationBddWriter<T> {
        specBuilder.addStep(type, description)
        return this
    }

    infix fun <R> run(block: (T) -> R): SpecificationBddWriter<R> {
        return SpecificationBddWriter(
            specBuilder = specBuilder,
            stepLastValue = block.invoke(stepLastValue)
        )
    }

    /**
     * Finish operation that returns Unit so test libs like Junit can work as expected.
     */
    infix fun runAndFinish(block: (T) -> Unit) {
        block.invoke(stepLastValue)
    }
}