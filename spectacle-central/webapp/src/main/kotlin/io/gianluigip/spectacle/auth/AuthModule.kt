package io.gianluigip.spectacle.auth

import react.*

data class AuthenticatedUser(
    val name: String,
    val username: String,
    val password: String,
)

typealias AuthState = StateInstance<AuthenticatedUser?>

val AuthContext = createContext<AuthState>()

val AuthModule = FC<PropsWithChildren> { props ->
    val user = useState<AuthenticatedUser>()

    AuthContext.Provider(user) {
        +props.children
    }
}
