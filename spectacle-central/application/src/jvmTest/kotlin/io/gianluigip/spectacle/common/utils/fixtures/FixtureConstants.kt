package io.gianluigip.spectacle.common.utils.fixtures

import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.TeamName
import java.time.ZonedDateTime

object FixtureConstants {
    val SOURCE = Source("Source1")
    val SOURCES: List<Source> = listOf(SOURCE)
    val FEATURE_NAME = FeatureName("Feature1")
    val TEAM_NAME = TeamName("Team1")
    val CREATION_TIME: ZonedDateTime = ZonedDateTime.now()
    val UPDATE_TIME: ZonedDateTime = ZonedDateTime.now()
}