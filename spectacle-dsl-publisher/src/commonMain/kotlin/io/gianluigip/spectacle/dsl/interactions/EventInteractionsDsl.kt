package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.specification.model.EventInteractionMetadata
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction

fun consumesEvent(eventName: String, metadata: EventInteractionMetadata? = null) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.INBOUND,
            type = InteractionType.EVENT,
            name = eventName,
            metadata = metadata?.toMap() ?: emptyMap()
        )
    )
}

fun producesEvent(eventName: String, metadata: EventInteractionMetadata? = null) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.OUTBOUND,
            type = InteractionType.EVENT,
            name = eventName,
            metadata = metadata?.toMap() ?: emptyMap()
        )
    )
}