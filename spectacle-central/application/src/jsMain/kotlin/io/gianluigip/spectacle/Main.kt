package io.gianluigip.spectacle

import csstype.Display
import csstype.GridArea
import csstype.GridTemplateAreas
import csstype.GridTemplateColumns
import csstype.GridTemplateRows
import csstype.Length
import io.gianluigip.spectacle.home.GridAreas
import io.gianluigip.spectacle.home.Sizes
import io.gianluigip.spectacle.home.ThemeModule
import io.gianluigip.spectacle.home.Content
import io.gianluigip.spectacle.navigation.component.Header
import kotlinext.js.jso
import kotlinx.browser.document
import mui.system.Box
import react.FC
import react.Props
import react.create
import react.dom.render
import react.router.dom.HashRouter

fun main() {
    render(
        element = App.create(),
        container = document.createElement("div").also { document.body!!.appendChild(it) },
    )
}

private val App = FC<Props> {
    HashRouter {
        ThemeModule {
            Box {
                sx = jso {
                    display = Display.grid
                    gridTemplateRows = GridTemplateRows(
                        Sizes.Header.Height, Length.auto,
                    )
                    gridTemplateColumns = GridTemplateColumns(
                        Length.auto,
                    )
                    gridTemplateAreas = GridTemplateAreas(
                        GridArea(GridAreas.Header),
                        GridArea(GridAreas.Content),
                    )
                }

                Header()
                Content()
            }
        }
    }
}
