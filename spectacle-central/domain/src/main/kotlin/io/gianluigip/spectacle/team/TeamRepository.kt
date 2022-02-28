package io.gianluigip.spectacle.team

import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.team.model.Team
import io.gianluigip.spectacle.team.model.TeamToDelete
import io.gianluigip.spectacle.team.model.TeamToUpsert

interface TeamRepository {

    fun findAll(): List<Team>

    fun findBySource(source: Source): List<Team>

    fun upsert(teams: List<TeamToUpsert>)

    fun delete(teamsToDelete: List<TeamToDelete>)

}
