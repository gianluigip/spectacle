package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import kotlinx.datetime.Instant

class SpecificationFinder(
    private val specRepo: SpecificationRepository,
    private val transaction: TransactionExecutor
) {

    fun findBy(
        searchText: String? = null,
        features: Set<FeatureName>? = null,
        sources: Set<Source>? = null,
        components: Set<Component>? = null,
        tags: Set<TagName>? = null,
        teams: Set<TeamName>? = null,
        status: Set<SpecStatus>? = null,
        updatedTimeAfter: Instant? = null,
    ): List<Specification> = transaction.execute { specRepo.findBy(searchText, features, sources, components, tags, teams, status, updatedTimeAfter) }

    fun findAll() = transaction.execute { specRepo.findAll() }
}
