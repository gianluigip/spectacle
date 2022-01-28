package io.gianluigip.spectacle.bdd

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
    tags: List<String> = mutableListOf(),
): SpecificationBddWriter<Unit> {
    //TODO register spec
    return SpecificationBddWriter(
        specBuilder = SpecificationBuilder(
            specName, featureName, featureDescription, team, status, tags.toMutableList()
        ),
        stepLastValue = Unit
    )
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
