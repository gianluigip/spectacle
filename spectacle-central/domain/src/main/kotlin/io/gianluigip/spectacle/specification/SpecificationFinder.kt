package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.InteractionType
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
        interactionComponents: Set<Component>? = null,
        interactionName: String? = null,
        interactionType: InteractionType? = null,
        tags: Set<TagName>? = null,
        teams: Set<TeamName>? = null,
        status: Set<SpecStatus>? = null,
        updatedTimeAfter: Instant? = null,
    ): List<Specification> = transaction.execute {
        specRepo.findBy(
            searchText = searchText,
            features = features,
            sources = sources,
            components = components,
            interactionComponents = interactionComponents,
            interactionName = interactionName,
            interactionType = interactionType,
            tags = tags,
            teams = teams,
            statuses = status,
            updatedTimeAfter = updatedTimeAfter
        )
    }

    fun findAll() = transaction.execute { specRepo.findAll() }
}
