package io.gianluigip.spectacle.common.utils.api

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.dsl.interactions.receivesGetRequest
import io.gianluigip.spectacle.dsl.interactions.receivesPutRequest
import io.gianluigip.spectacle.dsl.interactions.usesLibrary
import io.gianluigip.spectacle.specification.api.model.SpecificationResponse
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.ktor.client.call.body
import kotlinx.coroutines.runBlocking

fun BaseIntegrationTest.getSpecs(): List<SpecificationResponse> = runBlocking {
    usesLibrary("Spectacle DSL HTTP")
    receivesGetRequest("/api/specification", fromComponent = "Web UI").body()
}

fun BaseIntegrationTest.putSpecs(specs: SpecificationsToUpdateRequest) = runBlocking {
    usesLibrary("Spectacle DSL HTTP")
    receivesPutRequest(path = "/api/specification", body = specs, fromComponent = "Web UI")
}
