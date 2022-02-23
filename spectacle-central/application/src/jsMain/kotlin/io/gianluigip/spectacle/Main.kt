package io.gianluigip.spectacle

import csstype.Auto
import csstype.Display
import csstype.GridArea
import csstype.GridTemplateAreas
import csstype.GridTemplateRows
import io.gianluigip.spectacle.home.Content
import io.gianluigip.spectacle.home.GridAreas
import io.gianluigip.spectacle.home.Sizes
import io.gianluigip.spectacle.home.ThemeModule
import io.gianluigip.spectacle.navigation.component.Header
import kotlinx.browser.document
import kotlinx.js.jso
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
                    gridTemplateRows = "${Sizes.Header.Height} ${Auto.auto}".unsafeCast<GridTemplateRows>()
                    gridTemplateColumns = Auto.auto
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
