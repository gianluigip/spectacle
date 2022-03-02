package io.gianluigip.spectacle.auth.api

import io.gianluigip.spectacle.auth.api.model.AuthenticatedUserResponse
import io.gianluigip.spectacle.auth.api.model.LoginRequest
import io.gianluigip.spectacle.common.api.API_CLIENT
import io.gianluigip.spectacle.common.api.ENDPOINT
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

suspend fun postLogin(username: String, password: String): AuthenticatedUserResponse? {
    return try {
        API_CLIENT.post("$ENDPOINT/api/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }.body()
    } catch (ex: ClientRequestException) {
        if (ex.response.status == HttpStatusCode.Unauthorized) {
            null
        } else throw ex
    }
}