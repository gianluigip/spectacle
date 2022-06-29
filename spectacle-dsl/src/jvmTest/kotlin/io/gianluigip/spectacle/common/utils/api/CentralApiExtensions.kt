package io.gianluigip.spectacle.common.utils.api

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.delete
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.noContent
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.put
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import io.gianluigip.spectacle.BaseIntegrationTest
import io.gianluigip.spectacle.common.fixtures.AuthConstants.CENTRAL_PASSWORD
import io.gianluigip.spectacle.common.fixtures.AuthConstants.CENTRAL_USERNAME
import io.gianluigip.spectacle.dsl.interactions.sendsRequestTo
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun BaseIntegrationTest.stubPutSpecs() {
    configureMockServerClient()
    sendsRequestToCentral()
    // First request without auth
    stubFor(
        put("/api/specification").willReturn(
            aResponse().withStatus(401).withHeader("WWW-Authenticate", """Basic realm="Access", charset=UTF-8""")
        )
    )
    // Second request should include auth
    stubFor(
        put("/api/specification").withBasicAuth(CENTRAL_USERNAME, CENTRAL_PASSWORD).willReturn(noContent())
    )
}

fun BaseIntegrationTest.stubGetWiki(response: List<WikiPageMetadataResponse>, source: String = "spectacle-test") {
    configureMockServerClient()
    sendsRequestToCentral()
    stubFor(
        get("/api/wiki?sources=$source").willReturn(
            aResponse().withHeader("Content-Type", "application/json; charset=UTF-8")
                .withBody(Json.encodeToString(response))
                .withStatus(200)
        )
    )
}

fun BaseIntegrationTest.stubPostAnyWiki() {
    configureMockServerClient()
    sendsRequestToCentral()
    stubFor(
        post("/api/wiki").willReturn(noContent())
    )
}

fun BaseIntegrationTest.stubPutAnyWiki(wikiId: String) {
    configureMockServerClient()
    sendsRequestToCentral()
    stubFor(
        put("/api/wiki/$wikiId").willReturn(noContent())
    )
}

fun BaseIntegrationTest.stubDeleteAnyWiki(wikiId: String) {
    configureMockServerClient()
    sendsRequestToCentral()
    stubFor(
        delete("/api/wiki/$wikiId").willReturn(noContent())
    )
}

fun sendsRequestToCentral() = sendsRequestTo("Spectacle Central")
