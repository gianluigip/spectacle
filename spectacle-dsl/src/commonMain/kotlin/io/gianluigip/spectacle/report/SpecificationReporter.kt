package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.specification.Specification

expect object SpecificationReporter {
    fun initReport()
    fun registerSpec(spec: Specification)
    fun publishSpecs()
}