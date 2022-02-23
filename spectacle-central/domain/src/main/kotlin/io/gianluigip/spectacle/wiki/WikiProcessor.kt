package io.gianluigip.spectacle.wiki

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.wiki.model.WikiId
import io.gianluigip.spectacle.wiki.model.WikiPageMetadata
import io.gianluigip.spectacle.wiki.model.WikiPageToUpsert

class WikiProcessor(
    private val wikiRepo: WikiPageRepository,
    private val transaction: TransactionExecutor,
) {

    fun save(wiki: WikiPageToUpsert): WikiPageMetadata = transaction.execute {
        wikiRepo.save(wiki)
    }

    fun update(id: WikiId, wiki: WikiPageToUpsert): WikiPageMetadata = transaction.execute {
        wikiRepo.update(id, wiki)
    }

    fun delete(id: WikiId) = transaction.execute {
        wikiRepo.delete(id)
    }
}
