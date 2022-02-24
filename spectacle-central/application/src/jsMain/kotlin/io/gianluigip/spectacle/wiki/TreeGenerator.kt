package io.gianluigip.spectacle.wiki

import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse

data class ComponentWiki(
    val component: String,
    val rootDir: WikiDirectory,
)

data class WikiDirectory(
    val path: String,
    val name: String,
    val directories: List<WikiDirectory>,
    val pages: List<WikiPageMetadataResponse>,
)

private data class MutableWikiDirectory(
    val path: String,
    val name: String,
    val directories: MutableList<MutableWikiDirectory> = mutableListOf(),
    val pages: MutableList<WikiPageMetadataResponse> = mutableListOf(),
) {
    fun toImmutable(): WikiDirectory = WikiDirectory(
        path = path,
        name = name,
        directories = directories.map { it.toImmutable() }.sortedBy { it.name },
        pages = pages.sortedBy { it.fileName }
    )
}

object TreeGenerator {

    fun generateWikiTree(pages: List<WikiPageMetadataResponse>): List<ComponentWiki> {
        val componentWikis = mutableListOf<ComponentWiki>()
        pages.groupBy { it.component }.forEach { (component, componentPages) ->
            componentWikis += generateComponentWiki(component, componentPages)
        }
        return componentWikis.sortedBy { it.component }
    }

    private fun generateComponentWiki(component: String, pages: List<WikiPageMetadataResponse>): ComponentWiki {
        val directories = mutableMapOf<String, MutableWikiDirectory>()
        directories["/$component"] = MutableWikiDirectory(path = "/$component", name = component)

        pages.forEach { page ->
            val wikiPath = page.wikiPath.replace("/${page.fileName}", "")
            val pathSegments = wikiPath.split("/").run { if (last().isEmpty()) subList(0, size - 1) else this }
            var previousPath = ""

            pathSegments.forEachIndexed { i, segment ->
                if (segment.isEmpty()) return@forEachIndexed
                val currentPath = "$previousPath/$segment"

                val currentDir = if (directories.containsKey(currentPath)) {
                    directories[currentPath]!!
                } else {
                    val dir = MutableWikiDirectory(currentPath, segment)
                    directories[previousPath]!!.directories += dir
                    directories[currentPath] = dir
                    dir
                }
                if (i == (pathSegments.size - 1)) {
                    currentDir.pages += page
                }
                previousPath = currentPath
            }
        }
        return ComponentWiki(component, directories["/$component"]!!.toImmutable())
    }

}

val WikiPageResponse.fullPath: String
    get() = "${if (path.startsWith("/")) "" else "/"}$path/$fileName"

val WikiPageResponse.wikiPath: String get() = "/$component$fullPath"

val WikiPageMetadataResponse.fullPath: String
    get() = "${if (path.startsWith("/")) "" else "/"}$path/$fileName"

val WikiPageMetadataResponse.wikiPath: String get() = "/$component$fullPath"
