package io.gianluigip.spectacle.report.publisher

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.report.publisher.SpecificationStepFormatter.format
import io.gianluigip.spectacle.specification.SpecStatus
import io.gianluigip.spectacle.specification.Specification

object TerminalPublisher : SpecificationPublisher {

    init {
        SpecificationPublisher.registerPublisher("terminal", this)
    }

    override fun publishReport(specifications: List<Specification>, config: ReportConfiguration) {
        println("Publishing specification:")
        val specificationsByFeature = specifications.groupBy { it.metadata.featureName }.toSortedMap(Comparator.comparing { it ?: "" })
        specificationsByFeature.forEach { (features, specificationsInFeature) ->
            println(" ")
            println("Feature: $features")
            specificationsInFeature.forEach { specification ->
                println(" ")
                println("\t${specification.name}${specification.implementedText()}")
                specification.steps.forEach { step ->
                    println("\t\t${format(step)}")
                }
            }
            println(" ")
        }
        println(" ")
    }

    private fun Specification.implementedText(): String {
        return when (metadata.status) {
            SpecStatus.NOT_IMPLEMENTED -> {
                return " (Not Implemented)";
            }
            SpecStatus.PARTIALLY_IMPLEMENTED -> {
                return " (Partially Implemented)"
            }
            else -> {
                ""
            }
        }
    }
}
