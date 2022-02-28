package io.gianluigip.spectacle.home

import csstype.pct
import csstype.px
import io.gianluigip.spectacle.common.component.Spacer
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import io.gianluigip.spectacle.wiki.component.WikiDirectoryExplorer
import kotlinx.js.jso
import mui.material.Grid
import mui.material.GridDirection
import mui.material.Paper
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import react.FC
import react.Props
import react.useContext

val LandingView = FC<Props> {
    val theme by useContext(ThemeContext)

    Grid {
        container = true
        columnSpacing = ResponsiveStyleValue(SPACE_PADDING)
        direction = ResponsiveStyleValue(GridDirection.row)
        sx = jso { height = 100.pct }

        Grid {
            xs = 4; item = true
            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = "h5"; +"Wiki Explorer" }
                Spacer { height = 10.px }
                WikiDirectoryExplorer { }
            }
        }

        Grid {
            xs = 4; item = true
            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = "h5"; +"Features" }
                Spacer { height = 10.px }
            }
        }

        Grid {
            xs = 4; item = true
            Paper {
                sx = jso { padding = SPACE_PADDING; height = 100.pct }
                elevation = 2

                Typography { variant = "h5"; +"Components" }
                Spacer { height = 10.px }
            }
        }
    }
}

external interface ParagraphProps : Props {
    var text: String
}
