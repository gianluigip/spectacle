package io.gianluigip.spectacle.auth.components

import csstype.Color
import csstype.Display
import csstype.JustifyContent
import csstype.TextAlign
import csstype.px
import io.gianluigip.spectacle.auth.AuthContext
import io.gianluigip.spectacle.auth.AuthenticatedUser
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import kotlinx.js.jso
import mui.material.Alert
import mui.material.AlertColor.error
import mui.material.Box
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.Paper
import mui.material.Stack
import mui.material.TextField
import mui.material.Typography
import mui.system.ResponsiveStyleValue
import org.w3c.dom.HTMLButtonElement
import react.FC
import react.Props
import react.dom.events.MouseEvent
import react.dom.onChange
import react.router.useLocation
import react.router.useNavigate
import react.useContext
import react.useState

const val loginPath = "/login"
val LoginPage = FC<Props> {
    val theme by useContext(ThemeContext)
    val navigate = useNavigate()
    val location = useLocation()
    var user by useContext(AuthContext)

    var username by useState("")
    var password by useState("")
    var isLoginFailed by useState(false)

    val from: String = location.state.asDynamic()?.from?.pathname as? String ?: "/"

    fun onLogin(event: MouseEvent<HTMLButtonElement, *>) {
        val authenticatedUser = findUserFromCredentials(username, password)
        if (authenticatedUser == null) {
            isLoginFailed = true
            return
        }
        isLoginFailed = false
        user = authenticatedUser
        navigate.invoke(to = from, options = jso { replace = true })
    }

    Box {
        sx = jso {
            display = Display.flex; justifyContent = JustifyContent.center
        }

        Paper {
            sx = jso { width = 400.px; padding = SPACE_PADDING; }
            elevation = 2

            Typography {
                sx = jso { textAlign = TextAlign.center; color = Color(theme.palette.info.main) }
                variant = "h5"; +"Welcome to Spectacle Central";
            }
            Spacer { height = 10.px }

            Stack {
                spacing = ResponsiveStyleValue(2)

                if (isLoginFailed) Alert { severity = error; +"The credentials are invalid" }

                TextField {
                    id = "username"
                    label = "Username".toNode()
                    value = username
                    onChange = { newValue -> username = newValue.target.asDynamic().value as String }
                }
                TextField {
                    id = "password"
                    label = "Password".toNode()
                    value = password
                    onChange = { newValue -> password = newValue.target.asDynamic().value as String }
                }
                Button {
                    onClick = { onLogin(it) }
                    variant = ButtonVariant.contained
                    +"Login"
                }
            }
        }
    }
}

private fun findUserFromCredentials(username: String, password: String): AuthenticatedUser? {
    return if (username == password) {
        AuthenticatedUser("Guest", username, password)
    } else null
}
