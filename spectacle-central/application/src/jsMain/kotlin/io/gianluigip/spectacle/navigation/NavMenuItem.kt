package io.gianluigip.spectacle.navigation

import csstype.Color
import csstype.TextDecoration
import kotlinext.js.jso
import mui.material.Typography
import react.FC
import react.Props
import react.css.css
import react.dom.html.ReactHTML
import react.router.dom.NavLink
import react.router.useLocation

external interface NavMenuItemProps : Props {
    var toPath: String
    var label: String
    var textVariant: String?
}

val NavMenuItem = FC<NavMenuItemProps> {
    val pathname = useLocation().pathname
    val isHome = it.toPath == "/"
    val isSelectedPage = !isHome && pathname.contains(it.toPath)

    NavLink {
        to = it.toPath
        css {
            textDecoration = TextDecoration.none
            color = Color.currentcolor
        }

        Typography {
            sx = jso { if (isSelectedPage) textDecoration = TextDecoration.underline }
            it.textVariant?.let { value -> variant = value }
            noWrap = true
            component = ReactHTML.div

            +it.label
        }
    }
}
