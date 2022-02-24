package io.gianluigip.spectacle.wiki.component

import csstype.px
import io.gianluigip.spectacle.common.component.mermaidInit
import io.gianluigip.spectacle.diagram.utils.generateMermaidConfigHeader
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.wiki.component.markdown_plugins.rehypeHighlight
import io.gianluigip.spectacle.wiki.component.markdown_plugins.remarkGfm
import kotlinx.browser.document
import kotlinx.dom.createElement
import kotlinx.js.jso
import mui.material.Paper
import mui.material.styles.Theme
import org.w3c.dom.Element
import org.w3c.dom.get
import react.FC
import react.Props
import react.useContext
import react.useEffect

external interface MarkdownProps : Props {
    var content: String
}

val MarkdownViewer = FC<MarkdownProps> {
    val theme by useContext(ThemeContext)
    val content = it.content

    useEffect {
        renderMermaidDiagrams(theme)
    }

    ReactMarkdown {
        className = "markdown-body"
        remarkPlugins = arrayOf(remarkGfm)
        rehypePlugins = arrayOf(arrayOf(rehypeHighlight, jso<dynamic> { ignoreMissing = true }))
        +content
    }
}

private fun renderMermaidDiagrams(theme: Theme) {
    val diagramsCode = document.getElementsByClassName("language-mermaid")
    val diagramsCodeParents = mutableSetOf<Element>()

    for (i in 0 until diagramsCode.length) {
        val diagramCode = diagramsCode[i]
        diagramCode?.parentElement?.let {
            val codeParent = it
            codeParent.after(generateMermaidDiv(diagramCode.innerHTML, theme))
            diagramsCodeParents += codeParent
        }
    }
    try {
        mermaidInit()
        diagramsCodeParents.forEach { it.remove() }
    } catch (ex: dynamic) {
        println("The Mermaid Diagrams in the Wiki are invalid.")
    }
}

private fun generateMermaidDiv(diagramCode: String, theme: Theme): Element =
    document.createElement("div") {
        className = "mermaid"
        innerHTML = """
            ${generateMermaidConfigHeader(theme)}
            $diagramCode
        """.trimIndent()
    }
