package io.gianluigip.spectacle.navigation.logic

import io.gianluigip.spectacle.home.components.ErrorPage
import io.gianluigip.spectacle.home.components.Home
import js.core.ReadonlyArray
import js.core.jso
import react.create
import react.router.RouteObject

fun generateRoutes(): ReadonlyArray<RouteObject> = arrayOf(
    jso {
        path = "/"
        element = Home.create()
        errorElement = ErrorPage.create()
        children = generateRootChildrenRoutes()
    }
)

private fun generateRootChildrenRoutes(): ReadonlyArray<RouteObject> = mutableListOf<RouteObject>(
    jso {
        path = "/"
        element = Routes.home.root
        errorElement = ErrorPage.create()
    },
    jso {
        path = "*"
        element = ErrorPage.create()
    }
).apply {
    addAll(
        Routes.ALL.map { route ->
            jso {
                path = route.path
                element = route.root
                errorElement = ErrorPage.create()
            }
        }
    )
}.toTypedArray()
