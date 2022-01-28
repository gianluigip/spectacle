package io.gianluigip.spectacle.bdd.annotations

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Feature(
    val name: String,
    val description: String = ""
)
