package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.bdd.annotations.Feature
import io.gianluigip.spectacle.bdd.annotations.NotImplemented
import io.gianluigip.spectacle.bdd.annotations.SpecTags
import io.gianluigip.spectacle.bdd.annotations.Team
import io.gianluigip.spectacle.report.config.FileReportConfiguration
import io.gianluigip.spectacle.specification.SpecStatus
import io.gianluigip.spectacle.specification.SpecificationMetadata
import java.lang.reflect.Method

private const val DEFAULT_FEATURE = "Other"

object ContextGenerator {

    fun extractMetaData(testMethod: Method, externalTags: Set<String> = emptySet()): SpecificationMetadata? {
        if (isNotAnAcceptanceTest(testMethod)) {
            return null
        }
        val classAnnotations = testMethod.declaringClass.annotations
        val tags = mutableSetOf<String>().apply {
            addAll(externalTags)
            addAll(classAnnotations.getTags())
            addAll(testMethod.annotations.getTags())
        }
        return SpecificationMetadata(
            featureName = classAnnotations.getFeature(),
            featureDescription = classAnnotations.getFeatureDescription().removeStartingSpace(),
            team = classAnnotations.getTeam(),
            status = getSpecStatus(testMethod.annotations),
            tags = tags.toList().sortedBy { it }
        )
    }

    private fun isNotAnAcceptanceTest(testMethod: Method): Boolean =
        testMethod.annotations.find { it is io.gianluigip.spectacle.bdd.annotations.Specification } == null

    private fun Array<Annotation>.getFeature(): String =
        (this.find { it is Feature } as Feature?)?.name ?: DEFAULT_FEATURE

    private fun Array<Annotation>.getTags(): List<String> =
        (this.find { it is SpecTags } as SpecTags?)?.tags?.toList() ?: listOf()

    private fun Array<Annotation>.getFeatureDescription(): String =
        (this.find { it is Feature } as Feature?)?.description ?: ""

    private fun Array<Annotation>.getTeam(): String =
        (this.find { it is Team } as Team?)?.name ?: FileReportConfiguration.team

    private fun Array<Annotation>.isNotImplemented(): Boolean =
        (this.find { it is NotImplemented } as NotImplemented?)?.let { true } ?: false

    private fun getSpecStatus(methodAnnotations: Array<Annotation>): SpecStatus {
        return if (methodAnnotations.isNotImplemented()) {
            SpecStatus.NOT_IMPLEMENTED
        } else SpecStatus.IMPLEMENTED
    }

    private fun String.removeStartingSpace(): String = this.lines().map { it.trim() }.joinToString("\n")
}
