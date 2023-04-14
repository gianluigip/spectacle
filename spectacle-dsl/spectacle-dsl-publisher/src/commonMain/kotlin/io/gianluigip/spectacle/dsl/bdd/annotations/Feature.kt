package io.gianluigip.spectacle.dsl.bdd.annotations

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Feature(
    val name: String,
    val description: String = ""
)
