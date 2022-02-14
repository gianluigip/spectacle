package io.gianluigip.spectacle.navigation

import csstype.FlexGrow
import csstype.GridArea
import csstype.ZIndex
import csstype.px
import io.gianluigip.spectacle.home.GridAreas
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.common.component.Spacer
import io.gianluigip.spectacle.specification.component.specificationsReportPath
import kotlinext.js.jso
import mui.icons.material.Brightness4
import mui.icons.material.Brightness7
import mui.material.AppBar
import mui.material.AppBarPosition
import mui.material.Box
import mui.material.Switch
import mui.material.Toolbar
import mui.material.Tooltip
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.aria.ariaLabel
import react.useContext

val Header = FC<Props> {
    var theme by useContext(ThemeContext)

    AppBar {
        position = AppBarPosition.fixed
        sx = jso {
            gridArea = GridArea(GridAreas.Header)
            zIndex = ZIndex(1_500)
        }

        Toolbar {

            NavMenuItem {
                toPath = "/"
                label = "Spectacle Central"
                textVariant = "h6"
            }

            Spacer { width = 10.px }

            NavMenuItem {
                toPath = specificationsReportPath
                label = "Specifications"
            }

            Box { sx = jso { flexGrow = FlexGrow(1.0) } }

            Tooltip {
                title = ReactNode("Theme")

                Switch {
                    icon = Brightness7.create()
                    checkedIcon = Brightness4.create()
                    checked = theme == Themes.Dark
                    ariaLabel = "theme"

                    onChange = { _, checked ->
                        theme = if (checked) Themes.Dark else Themes.Light
                    }
                }
            }
        }
    }
}

