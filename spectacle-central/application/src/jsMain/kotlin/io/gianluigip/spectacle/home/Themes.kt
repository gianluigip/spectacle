package io.gianluigip.spectacle.home

import csstype.px
import kotlinx.js.jso
import mui.material.styles.createTheme

object Themes {

    val SPACE_PADDING = 20.px

    val Light = createTheme(
        jso {
            palette = jso { mode = "light" }
        }
    )

    val Dark = createTheme(
        jso {
            palette = jso { mode = "dark" }
        }
    )
}
