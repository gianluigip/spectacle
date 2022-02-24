package io.gianluigip.spectacle.wiki.component

import io.gianluigip.spectacle.common.component.LoadingBar
import io.gianluigip.spectacle.common.component.TreeViewSingleSelect
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.wiki.api.getAllPages
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.ReadonlyArray
import mui.icons.material.ChevronRight
import mui.icons.material.ExpandMore
import mui.lab.TreeItem
import react.FC
import react.Props
import react.create
import react.router.useNavigate
import react.useEffectOnce
import react.useState

external interface WikiDirectoryExplorerProps : Props {

}

val WikiDirectoryExplorer = FC<WikiDirectoryExplorerProps> {
    val navigate = useNavigate()

    var pages by useState<List<WikiPageMetadataResponse>>()
    var pageSelected by useState("")
    var directoriesExpanded by useState<ReadonlyArray<String>>(emptyArray())

    useEffectOnce {
        MainScope().launch { pages = getAllPages() }
    }

    LoadingBar { isLoading = pages == null }

    TreeViewSingleSelect {
        defaultCollapseIcon = ExpandMore.create()
        defaultExpandIcon = ChevronRight.create()
        multiSelect = false
        expanded = directoriesExpanded
        selected = pageSelected
        onNodeSelect = { _, node -> println("Selected $node"); pageSelected = node }
        onNodeToggle = { _, nodes -> println("Expanded $nodes"); directoriesExpanded = nodes }

        TreeItem {
            nodeId = "1"; label = "Application".toNode()
            TreeItem { nodeId = "2"; label = "Calendar".toNode() }
            TreeItem { nodeId = "3"; label = "Chrome".toNode() }
            TreeItem { nodeId = "4"; label = "Webstorm".toNode() }
        }
        TreeItem {
            nodeId = "5"; label = "Documents".toNode()
            TreeItem {
                nodeId = "6"; label = "MUI".toNode()
                TreeItem {
                    nodeId = "6"; label = "MUI".toNode()
                }
            }
        }
    }
}
