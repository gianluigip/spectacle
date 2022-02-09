package io.gianluigip.spectacle.specification.model

data class FeatureName(val value: String) {
    override fun toString(): String = value
}

fun String.toFeature() = FeatureName(this)

enum class SpecStatus {
    IMPLEMENTED, PARTIALLY_IMPLEMENTED, NOT_IMPLEMENTED
}

fun String.toSpecStatus() = SpecStatus.valueOf(this)

data class Source(val value: String) {
    override fun toString(): String = value
}

fun String.toSource() = Source(this)

data class Component(val value: String) {
    override fun toString(): String = value
}

fun String.toComponent() = Component(this)

data class SpecName(val value: String) {
    override fun toString(): String = value
}

fun String.toSpecName() = SpecName(this)

data class TeamName(val value: String) {
    override fun toString(): String {
        return value
    }
}

fun String.toTeam() = TeamName(this)

data class TagName(val value: String) {
    override fun toString(): String = value
}

fun String.toTag() = TagName(this)