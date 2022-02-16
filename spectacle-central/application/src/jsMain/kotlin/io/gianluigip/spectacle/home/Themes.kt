package io.gianluigip.spectacle.home

import kotlinext.js.jso
import mui.material.styles.createTheme

object Themes {
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