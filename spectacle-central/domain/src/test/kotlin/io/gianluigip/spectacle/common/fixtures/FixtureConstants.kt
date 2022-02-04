package io.gianluigip.spectacle.common.fixtures

import io.gianluigip.spectacle.specification.model.Source
import java.time.ZonedDateTime

object FixtureConstants {
    val SOURCE = Source("Source1")
    val SOURCES: List<Source> = listOf(SOURCE)
    val CREATION_TIME: ZonedDateTime = ZonedDateTime.now()
    val UPDATE_TIME: ZonedDateTime = ZonedDateTime.now()
}