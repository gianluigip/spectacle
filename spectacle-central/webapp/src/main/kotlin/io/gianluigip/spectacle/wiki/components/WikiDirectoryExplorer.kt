package io.gianluigip.spectacle.wiki.components

import csstype.px
import io.gianluigip.spectacle.common.components.LoadingBar
import io.gianluigip.spectacle.common.components.SearchTextField
import io.gianluigip.spectacle.common.components.TreeViewSingleSelect
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.wiki.ComponentWiki
import io.gianluigip.spectacle.wiki.TreeGenerator
import io.gianluigip.spectacle.wiki.WikiDirectory
import io.gianluigip.spectacle.wiki.api.getAllPages
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.wikiPath
import js.core.ReadonlyArray
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.ChevronRight
import mui.icons.material.ExpandMore
import mui.lab.TreeItem
import mui.material.Stack
import mui.material.styles.Theme
import mui.system.responsive
import react.*

data class WikiBrowserFilters(
    val searchText: String? = null
)

external interface WikiDirectoryExplorerProps : Props {
    var selectedPagePath: String?
    var expandAll: Boolean?
    var hideSearchBar: Boolean?
    var wikiFilters: WikiBrowserFilters?
    var onFiltersChanged: ((WikiBrowserFilters) -> Unit)?
    var onPageSelected: (WikiPageMetadataResponse) -> Unit
}

val WikiDirectoryExplorer = FC<WikiDirectoryExplorerProps> { props ->
    val theme by useRequiredContext(ThemeContext)

    var currentWikiFilters by useState<WikiBrowserFilters>()
    var wikiFilters = props.wikiFilters ?: WikiBrowserFilters()
    var componentWikis by useState<List<ComponentWiki>>()
    var pagesMap by useState<Map<String, WikiPageMetadataResponse>>()
    var pageSelected by useState(props.selectedPagePath ?: "")
    var directoriesExpanded by useState<ReadonlyArray<String>>(emptyArray())

    fun loadWikis(wikiFilters: WikiBrowserFilters) {
        componentWikis = null
        currentWikiFilters = wikiFilters
        MainScope().launch {
            val pages = getAllPages(filters = wikiFilters)
            val wikis = TreeGenerator.generateWikiTree(pages)
            componentWikis = wikis
            pagesMap = pages.associateBy { it.wikiPath }
            directoriesExpanded = if (props.expandAll == true) {
                findAllDirPaths(wikis)
            } else wikis.map { it.rootDir.path }.toTypedArray()
        }
    }

    useEffect {
        if (currentWikiFilters != wikiFilters) loadWikis(wikiFilters)
    }

    fun onPageSelected(pagePath: String) {
        pageSelected = pagePath
        pagesMap?.get(pagePath)?.let {
            props.onPageSelected(it)
        }
    }

    Stack {
        sx = jso { marginBottom = 20.px }
        spacing = responsive(1)

        if (props.hideSearchBar != true) {
            SearchTextField {
                label = "Search Bar"
                value = wikiFilters.searchText ?: ""
                onChange = { props.onFiltersChanged?.invoke(wikiFilters.copy(searchText = it)) }
            }
        }

        LoadingBar { isLoading = componentWikis == null }

        componentWikis?.let { compWikis ->
            TreeViewSingleSelect {
                defaultCollapseIcon = ExpandMore.create()
                defaultExpandIcon = ChevronRight.create()
                multiSelect = false
                expanded = directoriesExpanded
                selected = pageSelected
                onNodeSelect = { _, node -> onPageSelected(node) }
                onNodeToggle = { _, nodes -> directoriesExpanded = nodes }

                compWikis.forEach { componentWiki ->
                    +directorySection(componentWiki.rootDir, isTopFolder = true, theme = theme)
                }
            }
        }
    }
}

private fun directorySection(dir: WikiDirectory, isTopFolder: Boolean = false, theme: Theme): ReactNode {

    return TreeItem.create {
        nodeId = dir.path
        label = dir.name.toNode()

        ContentProps = jso {
            style = jso {
                if (isTopFolder) {
                    color = theme.palette.info.contrastText
                    backgroundColor = theme.palette.info.main
                } else {
                    color = theme.palette.info.main
                }
            }
        }

        dir.directories.forEach { nestedDir ->
            +directorySection(nestedDir, isTopFolder = false, theme)
        }

        dir.pages.forEach { page ->
            TreeItem {
                nodeId = page.wikiPath
                label = page.title.toNode()
            }
        }
    }
}

private fun findAllDirPaths(compWikis: List<ComponentWiki>): Array<String> {
    val paths = mutableSetOf<String>()
    compWikis.forEach {
        paths.addAll(findAllDirPaths(it.rootDir))
    }
    return paths.toTypedArray()
}

private fun findAllDirPaths(dir: WikiDirectory): MutableSet<String> {
    if (dir.directories.isEmpty()) return mutableSetOf(dir.path)
    val paths = mutableSetOf(dir.path)
    dir.directories.forEach {
        paths.addAll(findAllDirPaths(it))
    }
    return paths
}

