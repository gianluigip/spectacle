package io.gianluigip.spectacle.auth

import react.FC
import react.PropsWithChildren
import react.StateInstance
import react.createContext
import react.useState

data class AuthenticatedUser(
    val name: String,
    val username: String,
    val passwordHash: String,
)

typealias AuthState = StateInstance<AuthenticatedUser?>

val AuthContext = createContext<AuthState>()

val AuthModule = FC<PropsWithChildren> { props ->
    val user = useState<AuthenticatedUser>()
    println("User: ${user.component1()}")

    AuthContext.Provider(user) {
        props.children()
    }
}
