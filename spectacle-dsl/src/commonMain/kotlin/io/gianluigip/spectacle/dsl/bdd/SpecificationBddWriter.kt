package io.gianluigip.spectacle.dsl.bdd

import io.gianluigip.spectacle.specification.SpecificationBuilder
import io.gianluigip.spectacle.specification.model.StepType

/**
 * Provide a DSL for writing specifications as readable as possible.
 */
class SpecificationBddWriter<T>(
    val specBuilder: SpecificationBuilder,
    private val stepLastValue: T,
) {

    @BddDslMarker
    infix fun given(description: String) = addStep(StepType.GIVEN, description)

    @BddDslMarker
    infix fun andGiven(description: String) = addStep(StepType.AND_GIVEN, description)

    @BddDslMarker
    infix fun whenever(description: String) = addStep(StepType.WHENEVER, description)

    @BddDslMarker
    infix fun andWhenever(description: String) = addStep(StepType.AND_WHENEVER, description)

    @BddDslMarker
    infix fun then(description: String) = addStep(StepType.THEN, description)

    @BddDslMarker
    infix fun andThen(description: String) = addStep(StepType.AND_THEN, description)

    @BddDslMarker
    infix fun and(description: String) = addStep(StepType.AND, description)

    private fun addStep(type: StepType, description: String): SpecificationBddWriter<T> {
        specBuilder.addStep(type, description)
        return this
    }

    @BddDslMarker
    infix fun <R> run(block: (T) -> R): SpecificationBddWriter<R> {
        return SpecificationBddWriter(
            specBuilder = specBuilder,
            stepLastValue = block.invoke(stepLastValue)
        )
    }

    /**
     * Finish operation that returns Unit so test libs like Junit can work as expected.
     */
    @BddDslMarker
    infix fun runAndFinish(block: (T) -> Unit) {
        block.invoke(stepLastValue)
    }
}