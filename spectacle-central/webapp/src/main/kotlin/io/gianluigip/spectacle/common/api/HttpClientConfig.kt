package io.gianluigip.spectacle.common.api

import io.gianluigip.spectacle.auth.AuthenticatedUser
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window

private val locationOrigin = window.location.origin
val ENDPOINT = if (locationOrigin.contains("localhost") || locationOrigin.contains("0.0.0.0")) {
    "http://localhost:8080" // It allows to execute the server locally while executing the UI independently for faster feedback
} else locationOrigin

val API_CLIENT: HttpClient get() = _API_CLIENT

private var _API_CLIENT = HttpClient(Js) {
    install(ContentNegotiation) { json() }
}

fun updateApiClientWithCredentials(credentials: AuthenticatedUser?) {
    _API_CLIENT = HttpClient(Js) {
        install(ContentNegotiation) { json() }
        credentials?.let {
            install(Auth) {
                basic {
                    sendWithoutRequest { true }
                    credentials {
                        BasicAuthCredentials(username = credentials.username, password = credentials.password)
                    }
                }
            }
        }
    }
}
