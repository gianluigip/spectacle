package io.gianluigip.spectacle.components

import csstype.Color
import csstype.FlexGrow
import csstype.GridArea
import csstype.TextDecoration
import csstype.ZIndex
import csstype.px
import io.gianluigip.spectacle.common.component.GridAreas
import io.gianluigip.spectacle.common.theme.ThemeContext
import io.gianluigip.spectacle.common.theme.Themes
import io.gianluigip.spectacle.components.specification.specificationsReportPath
import kotlinext.js.jso
import mui.icons.material.Brightness4
import mui.icons.material.Brightness7
import mui.material.AppBar
import mui.material.AppBarPosition
import mui.material.Box
import mui.material.Switch
import mui.material.Toolbar
import mui.material.Tooltip
import mui.material.Typography
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.css.css
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML
import react.router.dom.NavLink
import react.router.useLocation
import react.useContext

val Header = FC<Props> {
    var theme by useContext(ThemeContext)
    val lastPathname = useLocation().pathname.substringAfterLast("/")

    AppBar {
        position = AppBarPosition.fixed
        sx = jso {
            gridArea = GridArea(GridAreas.Header)
            zIndex = ZIndex(1_500)
        }

        Toolbar {

            NavLink {
                to = "/"
                css {
                    textDecoration = TextDecoration.none
                    color = Color.currentcolor
                }

                Typography {
                    sx = jso { flexGrow = FlexGrow(1.0) }
                    variant = "h6"
                    noWrap = true
                    component = ReactHTML.div

                    +"Spectacle Central"
                }
            }

            NavLink {
                to = specificationsReportPath
                css {
                    paddingLeft = 10.px
                    textDecoration = TextDecoration.none
                    color = Color.currentcolor
                }

                Typography {
                    variant = "h7"
                    noWrap = true
                    component = ReactHTML.div

                    +"Specificationss"
                }
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

