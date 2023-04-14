package io.gianluigip.spectacle

import csstype.*
import io.gianluigip.spectacle.auth.AuthModule
import io.gianluigip.spectacle.home.Content
import io.gianluigip.spectacle.home.GridAreas
import io.gianluigip.spectacle.home.Sizes
import io.gianluigip.spectacle.home.ThemeModule
import io.gianluigip.spectacle.navigation.components.Header
import js.core.jso
import mui.system.Box
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.router.dom.HashRouter
import web.html.HTML

fun main() {
    val root = web.dom.document.createElement(HTML.div).also {
        it.className = "root-app"
        web.dom.document.body.appendChild(it)
    }
    createRoot(root).render(App.create())
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
