package io.gianluigip.spectacle.wiki.components

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.parseParams
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import io.gianluigip.spectacle.wiki.api.getWikiPage
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import io.gianluigip.spectacle.wiki.wikiPath
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Paper
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props
import react.router.useLocation
import react.useEffect
import react.useState

private const val EXPLORER_SIZE = 3
const val wikiPath = "/wiki"
val WikiBrowser = FC<Props> {

    val queryParams = useLocation().search.parseParams()
    val wikiId = queryParams["id"]
    var currentWikiId by useState("")
    var wikiPage by useState<WikiPageResponse>()
    var isLoading by useState(wikiId != null)

    fun loadPage(wikiId: String) {
        isLoading = true
        currentWikiId = wikiId
        MainScope().launch {
            wikiPage = getWikiPage(wikiId)
            isLoading = false
        }
    }

    useEffect { if (wikiId != null && currentWikiId != wikiId) loadPage(wikiId) }

    Grid {
        container = true
        direction = ResponsiveStyleValue(GridDirection.row)
        columnSpacing = ResponsiveStyleValue(SPACE_PADDING)
        sx = jso { height = 100.pct }

        Grid {
            item = true
            xs = EXPLORER_SIZE

            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = "h5"; +"Explorer" }
                Spacer { height = 10.px }
                WikiDirectoryExplorer { selectedPagePath = wikiPage?.wikiPath ?: "" }
            }
        }

        Grid {
            item = true
            xs = 12 - EXPLORER_SIZE

            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                LoadingBar { this.isLoading = isLoading }
                when {
                    wikiId == null -> MarkdownViewer { content = landingContent() }
                    !isLoading && wikiPage == null -> MarkdownViewer { content = notFoundContent() }
                    wikiPage != null -> wikiPage?.let { WikiPageViewer { wiki = it } }
                }
            }
        }
    }

}

private fun landingContent(): String = """
            # Spectacle Wiki
            Select any page to start.
        """.trimIndent()

private fun notFoundContent(): String = """
            # Page Not Found
            Select any page to start.
        """.trimIndent()
