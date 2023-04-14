package io.gianluigip.spectacle.dsl.bdd

import io.gianluigip.spectacle.specification.SpecificationBuilder

actual object TestContext {

    actual fun setCurrentSpec(spec: SpecificationBuilder) {
    }

    actual fun getCurrentSpec(): SpecificationBuilder? {
        return null
    }

    actual fun clearContext() {
    }
}