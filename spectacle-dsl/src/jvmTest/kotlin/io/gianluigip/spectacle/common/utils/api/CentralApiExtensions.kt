package io.gianluigip.spectacle.common.utils.api

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.noContent
import com.github.tomakehurst.wiremock.client.WireMock.put
import io.gianluigip.spectacle.dsl.interactions.sendsRequestTo

fun stubPutSpecs() {
    sendsRequestTo("Spectacle Central")
    WireMock.stubFor(
        put("/api/specification").willReturn(noContent())
    )
}
