package io.gianluigip.spectacle.auth

import io.gianluigip.spectacle.common.api.updateApiClientWithCredentials
import kotlinx.datetime.internal.JSJoda.use
import react.FC
import react.PropsWithChildren
import react.StateInstance
import react.createContext
import react.useState

data class AuthenticatedUser(
    val name: String,
    val username: String,
    val password: String,
)

typealias AuthState = StateInstance<AuthenticatedUser?>

val AuthContext = createContext<AuthState>()

val AuthModule = FC<PropsWithChildren> { props ->
    val user = useState<AuthenticatedUser?>(AuthenticatedUser(name = "Gigi", username = "admin", password = "admin"))

    user.component1()?.let { updateApiClientWithCredentials(it) }

    AuthContext.Provider(user) {
        +props.children
    }
}
