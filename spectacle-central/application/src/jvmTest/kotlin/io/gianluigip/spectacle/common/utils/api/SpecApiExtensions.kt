package io.gianluigip.spectacle.common.utils.api

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.specification.api.model.SpecificationResponse
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking

fun BaseIntegrationTest.getSpecs(): List<SpecificationResponse> = runBlocking {
    receivesRequestFromUI()
    httpClient.get("$httpHost/api/specification").body()
}

fun BaseIntegrationTest.putSpecs(specs: SpecificationsToUpdateRequest) = runBlocking {
    receivesRequestFromUI()
    httpClient.put("$httpHost/api/specification") {
        contentType(ContentType.Application.Json)
        setBody(specs)
    }
}
