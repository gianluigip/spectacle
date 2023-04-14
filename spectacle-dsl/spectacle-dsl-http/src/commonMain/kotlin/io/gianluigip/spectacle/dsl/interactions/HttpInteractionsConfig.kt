package io.gianluigip.spectacle.dsl.interactions

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

object HttpInteractionsConfig {
    internal var host = "http://localhost"
    internal var httpClient: HttpClient = HttpClient { }
}

fun httpInteractionsConfig(
    host: String,
    httpClient: HttpClient,
) {
    HttpInteractionsConfig.host = host
    HttpInteractionsConfig.httpClient = httpClient
}

fun httpInteractionsConfig(
    host: String,
    clientConfig: HttpClientConfig<*>.() -> Unit = {}
) = httpInteractionsConfig(
    host = host,
    httpClient = HttpClient {
        clientConfig(this)
    }
)
