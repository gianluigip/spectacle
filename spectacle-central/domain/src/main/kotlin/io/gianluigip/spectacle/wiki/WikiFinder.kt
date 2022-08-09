package io.gianluigip.spectacle.wiki

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.wiki.model.WikiId
import io.gianluigip.spectacle.wiki.model.WikiPageMetadata

class WikiFinder(
    private val wikiRepo: WikiPageRepository,
    private val transaction: TransactionExecutor,
) {

    fun findById(id: WikiId) = transaction.execute {
        wikiRepo.findById(id)
    }

    fun findBy(
        searchText: String? = null,
        features: Set<FeatureName>? = null,
        sources: Set<Source>? = null,
        components: Set<Component>? = null,
        tags: Set<TagName>? = null,
        teams: Set<TeamName>? = null,
    ): List<WikiPageMetadata> = transaction.execute {
        wikiRepo.findBy(searchText, features, sources, components, tags, teams)
    }

}
