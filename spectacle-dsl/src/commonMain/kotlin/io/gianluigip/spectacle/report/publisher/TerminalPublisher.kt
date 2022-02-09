package io.gianluigip.spectacle.report.publisher

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.report.publisher.SpecificationStepFormatter.format
import io.gianluigip.spectacle.specification.Specification
import io.gianluigip.spectacle.specification.model.SpecStatus

object TerminalPublisher : SpecificationPublisher {

    init {
        SpecificationPublisher.registerPublisher("terminal", this)
    }

    override suspend fun publishReport(specifications: List<Specification>, config: ReportConfiguration) {
        println(generateReportContent(specifications))
    }

    internal fun generateReportContent(specifications: List<Specification>): String {
        var report = "\nPublishing Specifications:\n"
        val specificationsByFeature = specifications.groupBy { it.metadata.featureName }.entries.sortedBy { it.key }
        specificationsByFeature.forEach { (features, specificationsInFeature) ->
            report += "\nFeature: $features\n"
            specificationsInFeature.sortedBy { it.name }.forEach { specification ->
                report += "\n\t${specification.name}${specification.implementedText()}\n"
                specification.steps.sortedBy { it.index }.forEach { step ->
                    report += "\t\t${format(step)}\n"
                }
            }
            report += "\n"
        }
        return report
    }

    private fun Specification.implementedText(): String {
        return when (metadata.status) {
            SpecStatus.NOT_IMPLEMENTED -> {
                return " (Not Implemented)"
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
