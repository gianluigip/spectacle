package io.gianluigip.spectacle.navigation.components

import csstype.Color
import csstype.None
import csstype.TextDecoration
import emotion.react.css
import kotlinx.js.jso
import mui.material.Typography
import mui.material.styles.TypographyVariant
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.dom.NavLink
import react.router.useLocation

external interface NavMenuItemProps : Props {
    var toPath: String
    var label: String
    var textVariant: TypographyVariant?
}

val NavMenuItem = FC<NavMenuItemProps> {
    val pathname = useLocation().pathname
    val isHome = it.toPath == "/"
    val isSelectedPage = !isHome && pathname.contains(it.toPath)

    NavLink {
        to = it.toPath
        css {
            textDecoration = None.none
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
