package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

class SpecificationFinder(
    private val specRepo: SpecificationRepository,
    private val transaction: TransactionExecutor
) {

    fun findBy(
        feature: FeatureName? = null,
        source: Source? = null,
        component: Component? = null,
        tag: TagName? = null,
        team: TeamName? = null,
        status: SpecStatus? = null,
    ) = transaction.execute { specRepo.findBy(feature, source, component, tag, team, status) }

    fun findAll() = transaction.execute { specRepo.findAll() }
}
