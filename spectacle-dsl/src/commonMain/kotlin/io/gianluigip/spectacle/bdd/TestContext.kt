package io.gianluigip.spectacle.bdd

import io.gianluigip.spectacle.specification.SpecificationBuilder

expect object TestContext {
    fun setCurrentSpec(spec: SpecificationBuilder)
    fun getCurrentSpec(): SpecificationBuilder?
    fun clearContext()
}