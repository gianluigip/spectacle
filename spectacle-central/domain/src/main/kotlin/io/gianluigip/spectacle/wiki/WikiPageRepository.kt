package io.gianluigip.spectacle.wiki

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.wiki.model.WikiId
import io.gianluigip.spectacle.wiki.model.WikiPage
import io.gianluigip.spectacle.wiki.model.WikiPageMetadata
import io.gianluigip.spectacle.wiki.model.WikiPageToUpsert

interface WikiPageRepository {

    fun findById(id: WikiId): WikiPage?

    fun findBy(
        features: Set<FeatureName>? = null,
        sources: Set<Source>? = null,
        components: Set<Component>? = null,
        tags: Set<TagName>? = null,
        teams: Set<TeamName>? = null,
    ):List<WikiPageMetadata>

    fun save(wiki: WikiPageToUpsert): WikiPageMetadata

    fun update(id: WikiId, wiki: WikiPageToUpsert): WikiPageMetadata

    fun delete(id: WikiId)

}
