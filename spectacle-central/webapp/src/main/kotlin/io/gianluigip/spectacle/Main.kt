package io.gianluigip.spectacle

import io.gianluigip.spectacle.home.ThemeModule
import io.gianluigip.spectacle.navigation.logic.generateRoutes
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.router.RouterProvider
import react.router.dom.createHashRouter
import web.html.HTML

fun main() {
    val root = web.dom.document.createElement(HTML.div).also {
        it.className = "root-app"
        web.dom.document.body.appendChild(it)
    }
    createRoot(root).render(App.create())
}

private val App = FC<Props> {

    val hashRouter = createHashRouter(
        routes = generateRoutes()
    )

    ThemeModule {
        RouterProvider {
            router = hashRouter
        }
    }
}
