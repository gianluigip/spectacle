package io.gianluigip.spectacle.wiki.component

import csstype.Color
import io.gianluigip.spectacle.common.component.LoadingBar
import io.gianluigip.spectacle.common.component.TreeViewSingleSelect
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.wiki.ComponentWiki
import io.gianluigip.spectacle.wiki.TreeGenerator
import io.gianluigip.spectacle.wiki.WikiDirectory
import io.gianluigip.spectacle.wiki.api.getAllPages
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.wikiPath
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.ReadonlyArray
import kotlinx.js.jso
import mui.icons.material.ChevronRight
import mui.icons.material.ExpandMore
import mui.lab.TreeItem
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.router.useNavigate
import react.useContext
import react.useEffectOnce
import react.useState

external interface WikiDirectoryExplorerProps : Props {
    var selectedPagePath: String?
}

val WikiDirectoryExplorer = FC<WikiDirectoryExplorerProps> {
    val theme by useContext(ThemeContext)
    val navigate = useNavigate()

    var componentWikis by useState<List<ComponentWiki>>()
    var pagesMap by useState<Map<String, WikiPageMetadataResponse>>()
    var pageSelected by useState(it.selectedPagePath)
    var directoriesExpanded by useState<ReadonlyArray<String>>(emptyArray())

    useEffectOnce {
        MainScope().launch {
            val pages = getAllPages()
            val wikis = TreeGenerator.generateWikiTree(pages)
            componentWikis = wikis
            pagesMap = pages.associateBy { it.wikiPath }
            directoriesExpanded = findAllDirPaths(wikis)
        }
    }
    fun navigateToPage(pagePath: String) {
        pageSelected = pagePath
        pagesMap?.get(pagePath)?.let {
            navigate.invoke("$wikiPath?id=${it.id}")
        }
    }

    LoadingBar { isLoading = componentWikis == null }

    componentWikis?.let { compWikis ->

        val topFolderCss = emotion.css.css(jso {
            color = Color(theme.palette.info.contrastText)
            backgroundColor = Color(theme.palette.info.main)
        })
        TreeViewSingleSelect {
            defaultCollapseIcon = ExpandMore.create()
            defaultExpandIcon = ChevronRight.create()
            multiSelect = false
            expanded = directoriesExpanded
            selected = pageSelected
            onNodeSelect = { _, node -> navigateToPage(node) }
            onNodeToggle = { _, nodes -> directoriesExpanded = nodes }

            compWikis.forEach { componentWiki ->
                +directorySection(componentWiki.rootDir, isTopFolder = true, topFolderCss = topFolderCss)
            }
        }
    }
}

private fun directorySection(dir: WikiDirectory, isTopFolder: Boolean = false, topFolderCss: String? = null): ReactNode {

    return TreeItem.create {
        nodeId = dir.path
        label = dir.name.toNode()
        if (isTopFolder && topFolderCss != null) {

//            val topLevelItemCss = emotion.css.css(jso {
//                color = Color(theme.palette.info.contrastText)
//                backgroundColor = Color(theme.palette.info.main)
//            })
//            css("TopLevelItem") {
//                color = Color(theme.palette.info.contrastText)
//                backgroundColor = Color(theme.palette.info.main)
//            }
            classes?.let {
                it.content = "${it.content} $topFolderCss"
            }
        }

        dir.directories.forEach { nestedDir ->
            +directorySection(nestedDir)
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

