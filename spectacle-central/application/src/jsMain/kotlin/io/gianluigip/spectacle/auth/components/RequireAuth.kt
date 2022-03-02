package io.gianluigip.spectacle.auth.components

import io.gianluigip.spectacle.auth.AuthContext
import kotlinx.js.jso
import react.FC
import react.PropsWithChildren
import react.router.Navigate
import react.router.useLocation
import react.useContext

external interface NavigationState {
    var from: String?
}

val RequireAuth = FC<PropsWithChildren> { props ->
    val location = useLocation()
    val user by useContext(AuthContext)

    if (user == null) {
        Navigate {
            to = loginPath
            replace = true
            state = jso<NavigationState> { from = location.pathname }
        }
        return@FC
    }
    props.children()
}
