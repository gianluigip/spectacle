package io.gianluigip.spectacle.team

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.team.model.Team

class TeamFinder(
    private val teamRepo: TeamRepository,
    private val transaction: TransactionExecutor
) {

    fun findAll(): List<Team> = transaction.execute { teamRepo.findAll() }
}