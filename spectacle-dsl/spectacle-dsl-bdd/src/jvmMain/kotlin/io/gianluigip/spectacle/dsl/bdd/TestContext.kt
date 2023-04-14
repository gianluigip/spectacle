package io.gianluigip.spectacle.dsl.bdd

import io.gianluigip.spectacle.specification.SpecificationBuilder

actual object TestContext {

    private val currentSpec = ThreadLocal<SpecificationBuilder?>()

    actual fun setCurrentSpec(spec: SpecificationBuilder) {
        currentSpec.set(spec)
    }

    actual fun getCurrentSpec(): SpecificationBuilder? = currentSpec.get()

    actual fun clearContext() {
        currentSpec.set(null)
    }
}
