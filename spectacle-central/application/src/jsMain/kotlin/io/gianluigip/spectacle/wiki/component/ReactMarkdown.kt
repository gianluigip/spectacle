@file:JsModule("react-markdown")
@file:JsNonModule

package io.gianluigip.spectacle.wiki.component

import react.Props

/**
 * https://github.com/remarkjs/react-markdown
 */

external interface ReactMarkdownProps : Props {
    var className: String?
    var remarkPlugins: Array<RemarkPlugin>?
    var rehypePlugins: Array<dynamic>?
}

external interface RemarkPlugin
external interface RehypePlugin

@JsName("default")
external val ReactMarkdown: react.FC<ReactMarkdownProps>
