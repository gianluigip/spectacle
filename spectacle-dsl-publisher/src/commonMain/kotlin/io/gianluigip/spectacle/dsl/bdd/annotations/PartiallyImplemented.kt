package io.gianluigip.spectacle.dsl.bdd.annotations

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PartiallyImplemented(
    val ticket: String = ""
)
