package io.gianluigip.spectacle.home

import csstype.px
import js.core.jso
import mui.material.PaletteMode
import mui.material.styles.createTheme

object Themes {

    val SPACE_PADDING = 20.px

    val Light = createTheme(
        jso {
            palette = jso { mode = PaletteMode.light }
        }
    )

    val Dark = createTheme(
        jso {
            palette = jso { mode = PaletteMode.dark }
        }
    )
}
