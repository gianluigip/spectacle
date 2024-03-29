package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction

fun libraryIsUsedIn(component: String) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.INBOUND,
            type = InteractionType.LIBRARY,
            name = component,
        )
    )
}

fun usesLibrary(libName: String) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.OUTBOUND,
            type = InteractionType.LIBRARY,
            name = libName,
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
