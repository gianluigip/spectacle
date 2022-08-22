package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import kotlinx.datetime.Instant

class SpecificationFinder(
    private val specRepo: SpecificationRepository,
    private val transaction: TransactionExecutor
) {

    fun findBy(
        searchText: String? = null,
        feature: Set<FeatureName>? = null,
        source: Set<Source>? = null,
        component: Set<Component>? = null,
        tag: Set<TagName>? = null,
        team: Set<TeamName>? = null,
        status: Set<SpecStatus>? = null,
        updatedTimeAfter: Instant? = null,
    ) = transaction.execute { specRepo.findBy(searchText, feature, source, component, tag, team, status, updatedTimeAfter) }

    fun findAll() = transaction.execute { specRepo.findAll() }
}
