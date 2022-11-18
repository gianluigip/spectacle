package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecToUpsert
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import kotlinx.datetime.Instant

interface SpecificationRepository {

    fun findAll(): List<Specification>

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
        statuses: Set<SpecStatus>? = null,
        updatedTimeAfter: Instant? = null,
    ): List<Specification>

    fun findBySource(source: Source): List<Specification>

    fun upsert(specs: List<SpecToUpsert>)

    fun delete(specs: List<Specification>)

}
