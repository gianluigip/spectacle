package io.gianluigip.spectacle.auth.components

import io.gianluigip.spectacle.auth.hooks.useAuthManager
import io.gianluigip.spectacle.navigation.logic.Paths.loginPath
import js.core.jso
import react.FC
import react.PropsWithChildren
import react.router.useLocation
import react.router.useNavigate
import react.useEffect
import remix.run.router.Location
import remix.run.router.LocationState

external interface NavigationState : LocationState {
    var from: Location?
}

val NavigationState.fromFullPath: String get() = if (from != null) "${from!!.pathname}${from!!.search}" else ""

val RequireAuth = FC<PropsWithChildren> { props ->
    val location = useLocation()
    val navigate = useNavigate()
    val user = useAuthManager().currentUser()

    useEffect {
        if (user == null) navigate(
            to = loginPath,
            options = jso {
                replace = true
                state = jso<NavigationState> { from = location }
            }
        )
    }

    if (user == null) return@FC

    +props.children
}
