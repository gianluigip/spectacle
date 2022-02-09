package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecToUpsert
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName

interface SpecificationRepository {

    fun findAll(): List<Specification>

    fun findBy(
        feature: FeatureName? = null,
        source: Source? = null,
        component: Component? = null,
        tag: TagName? = null,
        team: TeamName? = null,
        status: SpecStatus? = null,
    ): List<Specification>

    fun findBySource(source: Source): List<Specification>

    fun upsert(specs: List<SpecToUpsert>)

    fun delete(specs: List<Specification>)

}
