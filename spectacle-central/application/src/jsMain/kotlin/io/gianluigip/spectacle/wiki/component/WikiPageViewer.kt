package io.gianluigip.spectacle.wiki.component

import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import react.FC
import react.Props

external interface WikiPageProps : Props {
    var wiki: WikiPageResponse
}

val WikiPageViewer = FC<WikiPageProps> {

    MarkdownViewer {
        content = it.wiki.content
    }
}
