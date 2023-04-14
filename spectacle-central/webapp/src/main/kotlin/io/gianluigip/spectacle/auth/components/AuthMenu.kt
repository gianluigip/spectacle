package io.gianluigip.spectacle.auth.components

import csstype.px
import io.gianluigip.spectacle.auth.hooks.useAuthManager
import io.gianluigip.spectacle.common.utils.toNode
import js.core.jso
import mui.icons.material.Logout
import mui.material.*
import react.FC
import react.Props
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaControls
import react.dom.aria.ariaExpanded
import react.dom.aria.ariaHasPopup
import react.dom.events.MouseEvent
import react.useState
import web.dom.Element
import web.html.HTMLButtonElement

val AuthMenu = FC<Props> {
    val userManager = useAuthManager()
    var anchorEl by useState<Element>()
    val isOpen = anchorEl != null

    fun handleClick(event: MouseEvent<HTMLButtonElement, *>) {
        anchorEl = event.currentTarget
    }

    fun handleClose() {
        anchorEl = null
    }

    fun logout() {
        userManager.removeUser()
    }

    userManager.currentUser()?.let { user ->
        Tooltip {
            title = user.name.toNode()
            IconButton {
                onClick = { handleClick(it) }
                size = Size.small
                ariaControls = if (isOpen) "user-menu" else ""
                ariaHasPopup = AriaHasPopup.`true`
                ariaExpanded = isOpen

                Avatar { sx = jso { height = 30.px; width = 30.px } }
            }
        }
        Menu {
            id = "user-menu"
            this.anchorEl = { anchorEl?.asDynamic() }
            open = isOpen
            onClose = { handleClose() }
            onClick = { handleClose() }
            transformOrigin = jso { horizontal = "right"; vertical = "top" }
            anchorOrigin = jso { horizontal = "right"; vertical = "bottom" }

            MenuItem {
                ListItemIcon { Avatar { sx = jso { height = 25.px; width = 25.px } } }
                +user.name
            }
            Divider {}
            MenuItem {
                onClick = { logout() }
                ListItemIcon {
                    Logout { fontSize = SvgIconSize.small }
                }
                +"Logout"
            }
        }
    }
}
