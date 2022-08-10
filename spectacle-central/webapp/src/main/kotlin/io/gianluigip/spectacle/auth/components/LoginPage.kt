package io.gianluigip.spectacle.auth.components

import csstype.Display
import csstype.JustifyContent
import csstype.TextAlign
import csstype.px
import io.gianluigip.spectacle.auth.AuthContext
import io.gianluigip.spectacle.auth.AuthenticatedUser
import io.gianluigip.spectacle.auth.api.postLogin
import io.gianluigip.spectacle.common.components.Spacer
import io.gianluigip.spectacle.common.utils.toNode
import io.gianluigip.spectacle.home.ThemeContext
import io.gianluigip.spectacle.home.Themes.SPACE_PADDING
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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
import mui.material.styles.TypographyVariant.h5
import mui.system.responsive
import org.w3c.dom.HTMLFormElement
import react.FC
import react.Props
import react.dom.events.FormEvent
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.onChange
import react.router.useLocation
import react.router.useNavigate
import react.useContext
import react.useState

const val loginPath = "/login"
@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
val LoginPage = FC<Props> {
    val theme by useContext(ThemeContext)
    val navigate = useNavigate()
    val location = useLocation()
    var user by useContext(AuthContext)

    var username by useState("")
    var password by useState("")
    var isLoginFailed by useState(false)

    val from: String = (location.state as? NavigationState)?.fromFullPath ?: "/"

    fun onLogin(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        MainScope().launch {
            val authenticatedUser = findUserFromCredentials(username, password)
            if (authenticatedUser == null) {
                isLoginFailed = true
                return@launch
            }
            isLoginFailed = false
            user = authenticatedUser
            navigate.invoke(to = from, options = jso { replace = true })
        }
    }

    Box {
        sx = jso {
            display = Display.flex; justifyContent = JustifyContent.center
        }

        ReactHTML.form {
            onSubmit = { onLogin(it) }
            Paper {
                sx = jso { width = 400.px; padding = SPACE_PADDING; }
                elevation = 2

                Typography {
                    sx = jso { textAlign = TextAlign.center; color = theme.palette.info.main }
                    variant = h5; +"Welcome to Spectacle Central";
                }
                Spacer { height = 10.px }

                Stack {
                    spacing = responsive(2)

                    if (isLoginFailed) Alert { severity = error; +"The credentials are invalid" }

                    TextField {
                        id = "username"
                        label = "Username".toNode()
                        value = username
                        onChange = { newValue -> username = newValue.target.asDynamic().value as String }
                    }
                    TextField {
                        id = "password"
                        type = InputType.password
                        label = "Password".toNode()
                        value = password
                        onChange = { newValue -> password = newValue.target.asDynamic().value as String }
                    }
                    Button {
                        type = ButtonType.submit
                        variant = ButtonVariant.contained
                        +"Login"
                    }
                }
            }
        }
    }
}

suspend fun findUserFromCredentials(username: String, password: String): AuthenticatedUser? {
    val loginResult = postLogin(username, password)
    return if (loginResult != null) {
        AuthenticatedUser(loginResult.name, username, password)
    } else null
}
