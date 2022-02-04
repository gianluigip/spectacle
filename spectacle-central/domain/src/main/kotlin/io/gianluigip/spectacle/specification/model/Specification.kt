package io.gianluigip.spectacle.specification.model

import java.time.ZonedDateTime

@JvmInline
value class Source(val value: String) {
    override fun toString(): String {
        return value
    }
}

fun String.toSource() = Source(this)

@JvmInline
value class SpecName(val value: String) {
    override fun toString(): String {
        return value
    }
}

fun String.toSpecName() = SpecName(this)

@JvmInline
value class TagName(val value: String) {
    override fun toString(): String {
        return value
    }
}

fun String.toTag() = TagName(this)

data class Specification(
    val name: SpecName,
    val feature: FeatureName,
    val team: TeamName,
    val source: Source,
    val status: SpecStatus,
    val tags: List<TagName>,
    val steps: List<SpecificationStep>,
    val creationTime: ZonedDateTime = ZonedDateTime.now(),
    val updateTime: ZonedDateTime = ZonedDateTime.now(),
)

data class SpecificationStep(
    val type: StepType,
    val description: String,
    val index: Int,
)

enum class StepType {
    GIVEN, AND_GIVEN, WHENEVER, AND_WHENEVER, THEN, AND_THEN, AND
}

enum class SpecStatus {
    IMPLEMENTED, PARTIALLY_IMPLEMENTED, NOT_IMPLEMENTED
}

fun String.toSpecStatus() = SpecStatus.valueOf(this)
