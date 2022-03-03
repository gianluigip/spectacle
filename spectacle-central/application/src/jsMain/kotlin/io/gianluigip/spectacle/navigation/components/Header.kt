package io.gianluigip.spectacle.navigation.components

import csstype.FlexGrow
import csstype.GridArea
import csstype.None
import csstype.px
import io.gianluigip.spectacle.auth.components.AuthMenu
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.diagram.components.systemDiagramPath
import io.gianluigip.spectacle.feature.components.featuresPath
import io.gianluigip.spectacle.home.GridAreas
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.specification.components.specificationsReportPath
import io.gianluigip.spectacle.wiki.components.wikiPath
import kotlinx.browser.window
import kotlinx.js.jso
import mui.icons.material.Brightness4
import mui.icons.material.Brightness7
import mui.icons.material.GitHub
import mui.material.AppBar
import mui.material.AppBarPosition
import mui.material.Box
import mui.material.IconButton
import mui.material.IconButtonColor
import mui.material.Size
import mui.material.Switch
import mui.material.Toolbar
import mui.material.Tooltip
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaHasPopup
import react.dom.aria.ariaLabel
import react.useContext

const val GITHUB_URL = "https://github.com/gianluigip/spectacle"

val Header = FC<Props> {
    var theme by useContext(ThemeContext)

    AppBar {
        position = AppBarPosition.fixed
        sx = jso {
            gridArea = GridArea(GridAreas.Header)
        }

        Toolbar {

            NavMenuItem {
                toPath = "/"
                label = "Spectacle Central"
                textVariant = "h6"
            }

            Spacer { width = 10.px }
            NavMenuItem {
                toPath = featuresPath
                label = "Features"
            }

            Spacer { width = 10.px }
            NavMenuItem {
                toPath = specificationsReportPath
                label = "Specifications"
            }

            Spacer { width = 10.px }
            NavMenuItem {
                toPath = systemDiagramPath
                label = "System"
            }

            Spacer { width = 10.px }
            NavMenuItem {
                toPath = wikiPath
                label = "Wiki"
            }

            Box { sx = jso { flexGrow = FlexGrow(1.0) } }

            Tooltip {
                title = ReactNode("Theme")

                Switch {
                    sx = jso { display = None.none }
                    icon = Brightness7.create()
                    checkedIcon = Brightness4.create()
                    checked = theme == Themes.Dark
                    ariaLabel = "theme"

                    onChange = { _, checked ->
                        theme = if (checked) Themes.Dark else Themes.Light
                    }
                }
            }

            Tooltip {
                title = "Go to Source".toNode()
                IconButton {
                    ariaLabel = "Source Code"
                    ariaHasPopup = AriaHasPopup.`false`
                    size = Size.large
                    color = IconButtonColor.inherit
                    onClick = { window.location.href = GITHUB_URL }
                    GitHub()
                }
            }
            AuthMenu { }
        }
    }
}

