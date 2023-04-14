package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.report.config.ConfigLoader
import io.gianluigip.spectacle.specification.model.HttpInteractionMetadata
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction

fun receivesRequestFrom(
    componentName: String = ConfigLoader.CONFIG.component,
    metadata: HttpInteractionMetadata,
) = addInboundHttpInteraction(componentName, metadata)

fun receivesRequestFrom(componentName: String) = addInboundHttpInteraction(componentName, metadata = null)

fun sendsRequestTo(componentName: String) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.OUTBOUND,
            type = InteractionType.HTTP,
            name = componentName,
        )
    )
}

private fun addInboundHttpInteraction(fromComponent: String, metadata: HttpInteractionMetadata?) {
    TestContext.getCurrentSpec()?.addInteraction(
        SpecInteraction(
            direction = InteractionDirection.INBOUND,
            type = InteractionType.HTTP,
            name = fromComponent,
            metadata = metadata?.toMap() ?: emptyMap()
        )
    )
}
