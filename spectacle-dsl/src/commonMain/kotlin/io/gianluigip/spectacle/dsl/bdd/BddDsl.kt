package io.gianluigip.spectacle.dsl.bdd

import io.gianluigip.spectacle.specification.SpecStatus
import io.gianluigip.spectacle.specification.SpecificationBuilder

/**
 * Convenient way to start writing a spec if you don't want to use annotations or the Junit plugin.
 */
fun aSpec(
    specName: String? = null,
    featureName: String? = null,
    featureDescription: String? = null,
    team: String? = null,
    status: SpecStatus? = null,
    tags: Set<String> = mutableSetOf(),
): SpecificationBddWriter<Unit> {
    val specWriter = SpecificationBddWriter(
        specBuilder = SpecificationBuilder(
            specName, featureName, featureDescription, team, status, tags.toMutableSet()
        ),
        stepLastValue = Unit
    )
    TestContext.setCurrentSpec(specWriter.specBuilder)
    return specWriter
}

/**
 * Quick way to start writing a spec, works better when using annotations and the Junit plugin to collect the metadata.
 */
fun <R> given(description: String, block: (Unit) -> R): SpecificationBddWriter<R> {
    return aSpec().given(description).run(block)
}

/**
 * Quick way to start writing a spec, works better when using annotations and the Junit plugin to collect the metadata.
 */
fun <R> whenever(description: String, block: (Unit) -> R): SpecificationBddWriter<R> {
    return aSpec().whenever(description).run(block)
}
