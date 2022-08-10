package io.gianluigip.spectacle

import csstype.Auto
import csstype.Display
import csstype.GridArea
import csstype.GridTemplateAreas
import csstype.GridTemplateRows
import csstype.pct
import io.gianluigip.spectacle.auth.AuthModule
import io.gianluigip.spectacle.home.Content
import io.gianluigip.spectacle.home.GridAreas
import io.gianluigip.spectacle.home.Sizes
import io.gianluigip.spectacle.home.ThemeModule
import io.gianluigip.spectacle.navigation.components.Header
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
        container = document.createElement("div").also {
            it.className = "root-app"
            document.body!!.appendChild(it)
        },
    )
}

private val App = FC<Props> {
    HashRouter {
        AuthModule {
            ThemeModule {
                Box {
                    sx = jso {
                        display = Display.grid
                        height = 100.pct
                        gridTemplateRows = "${Sizes.Header.Height} ${Auto.auto}".unsafeCast<GridTemplateRows>()
                        gridTemplateColumns = Auto.auto
                        gridTemplateAreas = GridTemplateAreas(
                            GridAreas.Header,
                            GridAreas.Content,
                        )
                    }

                    Header()
                    Content()
                }
            }
        }
    }
}
