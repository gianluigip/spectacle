package io.gianluigip.spectacle.common.fixtures

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.HttpInteractionMetadata
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.toComponent

fun aHttpInteraction(
    direction: InteractionDirection = INBOUND,
    name: Component = "Test".toComponent(),
    metadata: HttpInteractionMetadata,
) = SpecInteraction(
    direction = direction,
    type = InteractionType.HTTP,
    name = name.toString(),
    metadata = metadata.toMap()
)

fun aHttpMetadata(
    path: String = "/api/specs",
    method: String = "GET",
    queryParameters: Map<String, String> = emptyMap(),
    requestBody: String? = null,
    requestContentType: String? = "application/json",
    responseBody: String = """[ { "field1": "val1" } ]""",
    responseStatus: String = "200",
    responseContentType: String? = "application/json",
) = HttpInteractionMetadata(
    path, method, queryParameters, requestBody, requestContentType, responseBody, responseStatus, responseContentType
)
