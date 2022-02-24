package io.gianluigip.spectacle.wiki.component

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.component.LoadingBar
import io.gianluigip.spectacle.common.component.Spacer
import io.gianluigip.spectacle.common.utils.parseParams
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
    var wikiPage by useState<WikiPageResponse>()
    var isLoading by useState(wikiId != null)

    fun loadPage(wikiId: String) {
        isLoading = true
        MainScope().launch {
            wikiPage = getWikiPage(wikiId)
            isLoading = false
        }
    }

    useEffect { if (wikiId != null && wikiId != wikiPage?.id) loadPage(wikiId) }

    Grid {
        container = true
        spacing = ResponsiveStyleValue(20.px)
        direction = ResponsiveStyleValue(GridDirection.row)
        sx = jso { height = 100.pct }

        Grid {
            item = true
            xs = EXPLORER_SIZE
            sx = jso { height = 100.pct }

            Paper {
                sx = jso { padding = 20.px; height = 100.pct; height = 100.pct }
                elevation = 2

                Typography { variant = "h5"; +"Explorer" }
                Spacer { height = 10.px }
                WikiDirectoryExplorer { selectedPagePath = wikiPage?.wikiPath ?: "" }
            }
        }

        Grid {
            item = true
            xs = 12 - EXPLORER_SIZE
            sx = jso { height = 100.pct }
            Paper {
                sx = jso { padding = 20.px; height = 100.pct }
                elevation = 2

                LoadingBar { isLoading = wikiId != null && wikiId != wikiPage?.id }
                when {
                    wikiId == null -> MarkdownViewer { content = landingContent() }
                    !isLoading && wikiPage == null -> MarkdownViewer { content = NotFounContent() }
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

private fun NotFounContent(): String = """
            # Page Not Found
            Select any page to start.
        """.trimIndent()
