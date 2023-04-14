package io.gianluigip.spectacle.dsl.bdd.annotations

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Team(
    val name: String
)
