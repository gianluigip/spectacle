package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest

object CentralWikiPublisher {

    suspend fun publishWiki(centralClient: CentralClient, config: ReportConfiguration) {
        if (!config.centralConfig.wikiEnabled || config.centralConfig.localWikiLocation == null) {
            println("Skipping Central Wiki publisher because it is disable.")
            return
        }
        val localPages = WikiFilesLoader.loadWikiPages(config)
        val centralPages = centralClient.getWikiPages(config.source)
        syncLocalPages(localPages, centralPages, centralClient, config)
    }
}

private suspend fun syncLocalPages(
    localPages: List<WikiPageRequest>, centralPages: List<WikiPageMetadataResponse>, centralClient: CentralClient, config: ReportConfiguration
) {
    val localMap = localPages.associateBy { it.fullPath() }
    val centralMap = centralPages.associateBy { it.fullPath() }

    deleteCentralPagesThatDoesntExistInLocal(localMap, centralPages, centralClient, config)
    upsertLocalPages(localPages, centralMap, centralClient, config)
}

private suspend fun deleteCentralPagesThatDoesntExistInLocal(
    localMap: Map<String, WikiPageRequest>, centralPages: List<WikiPageMetadataResponse>, centralClient: CentralClient, config: ReportConfiguration
) {
    centralPages.forEach { centralPage ->
        val centralPagePath = centralPage.fullPath()
        if (!localMap.containsKey(centralPage.fullPath())) {
            println("Deleting the central wiki page $centralPagePath")
            centralClient.deleteWikiPage(centralPage.id)
        }
    }
}

private suspend fun upsertLocalPages(
    localPages: List<WikiPageRequest>, centralMap: Map<String, WikiPageMetadataResponse>, centralClient: CentralClient, config: ReportConfiguration
) {
    localPages.forEach { localPage ->
        val localPagePath = localPage.fullPath()
        if (centralMap.containsKey(localPagePath)) {
            val centralPage = centralMap[localPagePath]!!
            if (localPage.isMetadataEquals(centralPage)) {
                println("Skipping wiki page because is the same $localPagePath")
            } else {
                println("Updating the wiki page $localPagePath")
                centralClient.putWikiPage(localPage, centralPage.id)
            }
        } else {
            println("Storing the new wiki page $localPagePath")
            centralClient.postWikiPage(localPage)
        }
    }
}

private fun WikiPageRequest.isMetadataEquals(storedWiki: WikiPageMetadataResponse): Boolean =
    title == storedWiki.title && path == storedWiki.path && fileName == storedWiki.fileName && checksum == storedWiki.checksum &&
            team == storedWiki.team && tags.size == storedWiki.tags.size && tags.containsAll(storedWiki.tags) &&
            features.size == storedWiki.features.size && features.containsAll(storedWiki.features) &&
            source == storedWiki.source && component == storedWiki.component
