package io.gianluigip.spectacle.auth.components

import history.Location
import history.LocationState
import io.gianluigip.spectacle.auth.AuthContext
import js.core.jso
import react.FC
import react.PropsWithChildren
import react.router.Navigate
import react.router.useLocation
import react.useRequiredContext

external interface NavigationState {
    var from: Location?
}

val NavigationState.fromFullPath: String get() = if (from != null) "${from!!.pathname}${from!!.search}" else ""

val RequireAuth = FC<PropsWithChildren> { props ->
    val location = useLocation()
    val user by useRequiredContext(AuthContext)

    if (user == null) {
        Navigate {
            to = loginPath
            replace = true
            state = jso<NavigationState> { from = location }.unsafeCast<LocationState>()
        }
        return@FC
    }
    +props.children
}
