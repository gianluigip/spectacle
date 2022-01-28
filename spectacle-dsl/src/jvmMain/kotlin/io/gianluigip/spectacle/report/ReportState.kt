package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.specification.Specification

object ReportState {

    private val specs: MutableList<Specification> = mutableListOf()

    @Synchronized
    fun registerSpec(test: Specification) {
        specs.add(test)
    }

    fun registeredSpecs(): List<Specification> = specs
}