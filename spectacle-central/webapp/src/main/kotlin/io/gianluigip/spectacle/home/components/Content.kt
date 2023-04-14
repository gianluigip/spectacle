package io.gianluigip.spectacle.home.components

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.home.GridAreas
import js.core.jso
import mui.material.Paper
import mui.material.PaperVariant
import mui.system.Box
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.Outlet

private val DEFAULT_PADDING = 10.px

val Content = FC<Props> {

    Box {
        component = ReactHTML.main
        sx = jso {
            height = 100.pct
            gridArea = GridAreas.Content
            padding = DEFAULT_PADDING
        }

        Paper {
            sx = jso {
                padding = DEFAULT_PADDING
                height = 100.pct
            }
            variant = PaperVariant.elevation
            elevation = 0

            Outlet()
        }
    }
}
