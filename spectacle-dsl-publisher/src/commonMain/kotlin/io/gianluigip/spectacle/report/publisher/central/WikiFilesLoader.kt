package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.report.config.ReportConfiguration
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest

expect object WikiFilesLoader {
    fun loadWikiPages(config: ReportConfiguration): List<WikiPageRequest>
}