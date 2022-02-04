package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.Team
import io.gianluigip.spectacle.specification.model.TeamToDelete
import io.gianluigip.spectacle.specification.model.TeamToUpsert

interface TeamRepository {

    fun findAll(): List<Team>

    fun findBySource(source: Source): List<Team>

    fun upsert(teams: List<TeamToUpsert>)

    fun delete(teamsToDelete: List<TeamToDelete>)

}
