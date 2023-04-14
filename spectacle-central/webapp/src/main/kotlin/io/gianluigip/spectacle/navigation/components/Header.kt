package io.gianluigip.spectacle.navigation.components

import csstype.None
import csstype.number
import csstype.px
import io.gianluigip.spectacle.auth.components.AuthMenu
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.home.GridAreas
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes
import io.gianluigip.spectacle.navigation.logic.Routes
import js.core.jso
import kotlinx.browser.window
import mui.icons.material.Brightness4
import mui.icons.material.Brightness7
import mui.icons.material.GitHub
import mui.material.*
import mui.material.styles.TypographyVariant.Companion.h6
import react.*
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaHasPopup
import react.dom.aria.ariaLabel

const val GITHUB_URL = "https://github.com/gianluigip/spectacle"

val Header = FC<Props> {
    var theme by useRequiredContext(ThemeContext)

    AppBar {
        position = AppBarPosition.fixed
        sx = jso {
            gridArea = GridAreas.Header
        }

        Toolbar {

            NavMenuItem {
                toPath = "/"
                label = "Spectacle Central"
                textVariant = h6
            }

            Routes.MENU.forEach { route ->
                Spacer { width = 10.px }
                NavMenuItem {
                    toPath = route.path
                    label = route.name
                }
            }

            Box { sx = jso { flexGrow = number(1.0) } }

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

