package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction

fun consumesEvent(eventName: String) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.INBOUND,
            type = InteractionType.EVENT,
            name = eventName,
        )
    )
}

fun producesEvent(eventName: String) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.OUTBOUND,
            type = InteractionType.EVENT,
            name = eventName,
        )
    )
}

fun receivesRequestFrom(componentName: String) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.INBOUND,
            type = InteractionType.HTTP,
            name = componentName,
        )
    )
}


fun sendsRequestTo(componentName: String) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.OUTBOUND,
            type = InteractionType.HTTP,
            name = componentName,
        )
    )
}

fun usesPersistence(databaseName: String) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.OUTBOUND,
            type = InteractionType.PERSISTENCE,
            name = databaseName,
        )
    )
}
