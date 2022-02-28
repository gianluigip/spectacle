package io.gianluigip.spectacle.common.fixtures

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.team.model.Team
import io.gianluigip.spectacle.specification.model.TeamName
import java.time.ZonedDateTime

object TeamConstants {
    val TEAM_NAME = TeamName("Team1")
}

fun aTeam(
    name: TeamName = TeamConstants.TEAM_NAME,
    sources: List<Source> = FixtureConstants.SOURCES,
    components: List<Component> = FixtureConstants.COMPONENTS,
    creationTime: ZonedDateTime = FixtureConstants.CREATION_TIME,
    updateTime: ZonedDateTime = FixtureConstants.UPDATE_TIME,
) = Team(name, sources, components, creationTime, updateTime)