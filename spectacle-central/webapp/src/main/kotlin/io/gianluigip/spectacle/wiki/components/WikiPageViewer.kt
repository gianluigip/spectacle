package io.gianluigip.spectacle.wiki.components

import csstype.px
import io.gianluigip.spectacle.common.components.MetaDataChip
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toDisplay
import io.gianluigip.spectacle.wiki.api.model.WikiPageResponse
import mui.material.Grid
import mui.material.GridDirection
import mui.system.responsive
import react.FC
import react.Props

external interface WikiPageProps : Props {
    var wiki: WikiPageResponse
}

val WikiPageViewer = FC<WikiPageProps> {
    val wiki = it.wiki

    Grid {
        container = true
        spacing = responsive(1)
        direction = responsive(GridDirection.row)

        if (wiki.features.isNotEmpty()) {
            Grid { item = true; MetaDataChip { label = "Features: ${wiki.features.sorted().joinToString(", ")}" } }
        }
        if (wiki.tags.isNotEmpty()) {
            Grid { item = true; MetaDataChip { label = "Tags: ${wiki.tags.sorted().joinToString(", ")}" } }
        }
        Grid { item = true; MetaDataChip { label = "Team: ${wiki.team}" } }
        Grid { item = true; MetaDataChip { label = "Created: ${wiki.creationTime.toDisplay()}" } }
        Grid { item = true; MetaDataChip { label = "Updated: ${wiki.updateTime.toDisplay()}" } }
        Grid { item = true; MetaDataChip { label = "Component: ${wiki.component}" } }
        Grid { item = true; MetaDataChip { label = "Source: ${wiki.source}" } }
    }
    Spacer { height = 5.px }

    MarkdownViewer {
        content = wiki.content
    }
}
