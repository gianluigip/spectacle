package io.gianluigip.spectacle.dsl.bdd.annotations

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class SpecTags(
    vararg val tags: String,
)
