package io.gianluigip.spectacle.specification.model

data class FeatureName(val value: String) {
    override fun toString(): String = value
}

fun String.toFeature() = FeatureName(this)

enum class SpecStatus(val display: String) {
    IMPLEMENTED("Implemented"), PARTIALLY_IMPLEMENTED("Partially Implemented"), NOT_IMPLEMENTED("Not Implemented");

    companion object {
        fun fromDisplay(value: String): SpecStatus? = values().firstOrNull { it.display == value }
    }
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