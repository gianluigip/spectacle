package io.gianluigip.spectacle.bdd.annotations

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PartiallyImplemented(
    val ticket: String = ""
)
