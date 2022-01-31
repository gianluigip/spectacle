package io.gianluigip.spectacle.dsl.bdd.annotations

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class NotImplemented(
    val ticket: String = ""
)
