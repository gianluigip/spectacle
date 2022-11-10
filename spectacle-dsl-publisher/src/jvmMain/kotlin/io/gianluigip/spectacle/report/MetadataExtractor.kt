package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.NotImplemented
import io.gianluigip.spectacle.dsl.bdd.annotations.PartiallyImplemented
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.annotations.Team
import io.gianluigip.spectacle.report.config.ConfigLoader.CONFIG
import io.gianluigip.spectacle.specification.SpecificationMetadata
import io.gianluigip.spectacle.specification.model.SpecStatus
import java.lang.reflect.Method

object MetadataExtractor {

    private const val DEFAULT_FEATURE = "Other"

    fun extract(testMethod: Method, externalTags: Set<String> = emptySet()): SpecificationMetadata? {
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
        testMethod.annotations.find { it is Specification } == null

    private fun Array<Annotation>.getFeature(): String =
        (this.find { it is Feature } as Feature?)?.name ?: DEFAULT_FEATURE

    private fun Array<Annotation>.getTags(): List<String> =
        (this.find { it is SpecTags } as SpecTags?)?.tags?.toList() ?: listOf()

    private fun Array<Annotation>.getFeatureDescription(): String =
        (this.find { it is Feature } as Feature?)?.description ?: ""

    private fun Array<Annotation>.getTeam(): String =
        (this.find { it is Team } as Team?)?.name ?: CONFIG.team

    private fun Array<Annotation>.isNotImplemented(): Boolean =
        (this.find { it is NotImplemented } as NotImplemented?)?.let { true } ?: false

    private fun Array<Annotation>.isPartiallyImplemented(): Boolean =
        (this.find { it is PartiallyImplemented } as PartiallyImplemented?)?.let { true } ?: false

    private fun getSpecStatus(methodAnnotations: Array<Annotation>): SpecStatus {
        return when {
            methodAnnotations.isNotImplemented() -> SpecStatus.NOT_IMPLEMENTED
            methodAnnotations.isPartiallyImplemented() -> SpecStatus.PARTIALLY_IMPLEMENTED
            else -> SpecStatus.IMPLEMENTED
        }
    }

    private fun String.removeStartingSpace(): String = this.lines().map { it.trim() }.joinToString("\n")
}
