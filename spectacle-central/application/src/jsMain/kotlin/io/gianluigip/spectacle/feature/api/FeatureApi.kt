package io.gianluigip.spectacle.feature.api

import io.gianluigip.spectacle.common.api.API_CLIENT
import io.gianluigip.spectacle.common.api.ENDPOINT
import io.gianluigip.spectacle.feature.api.model.FeatureResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend fun getFeatures(): List<FeatureResponse> =
    API_CLIENT.get("$ENDPOINT/api/features").body()
