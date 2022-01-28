package io.gianluigip.spectacle.bdd

import io.gianluigip.spectacle.specification.SpecificationBuilder

private val currentSpec = ThreadLocal<SpecificationBuilder?>()

actual object TestContext {

    actual fun setCurrentSpec(spec: SpecificationBuilder) {
        currentSpec.set(spec)
    }

    actual fun getCurrentSpec(): SpecificationBuilder? = currentSpec.get()

    actual fun clearContext() {
        currentSpec.set(null)
    }
}