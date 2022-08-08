package io.gianluigip.spectacle.common.api

import io.gianluigip.spectacle.auth.AuthenticatedUser
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.window

private val locationOrigin = window.location.origin
val ENDPOINT = if (locationOrigin.contains("localhost") || locationOrigin.contains("0.0.0.0")) {
    "http://localhost:8080" // It allows to execute the server locally while executing the UI independently for faster feedback
} else locationOrigin

val API_CLIENT: HttpClient get() = _API_CLIENT

private var _API_CLIENT = HttpClient(Js) {
    install(ContentNegotiation) { json() }
}

fun updateApiClientWithCredentials(credentials: AuthenticatedUser) {
    _API_CLIENT = HttpClient(Js) {
        install(ContentNegotiation) { json() }
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
