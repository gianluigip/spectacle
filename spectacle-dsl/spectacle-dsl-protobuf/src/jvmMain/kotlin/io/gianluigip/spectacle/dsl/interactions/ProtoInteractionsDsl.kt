package io.gianluigip.spectacle.dsl.interactions

import com.google.protobuf.GeneratedMessageV3
import io.gianluigip.spectacle.specification.model.EventFormat
import io.gianluigip.spectacle.specification.model.EventInteractionMetadata

fun <T : GeneratedMessageV3> consumesProtoEvent(event: T, block: (T) -> Unit = {}) {
    val descriptor = event.descriptorForType
    consumesEvent(
        eventName = descriptor.name,
        metadata = EventInteractionMetadata(
            format = EventFormat.PROTOBUF,
            schema = descriptor.file.toProto().toString().trim(),
            dependencies = descriptor.file.dependencies.map { it.toProto().toString().trim() }
        )
    )
    block(event)
}

fun <T : GeneratedMessageV3> producesProtoEvent(event: T, block: (T) -> Unit = {}) {
    val descriptor = event.descriptorForType
    producesEvent(
        eventName = descriptor.name,
        metadata = EventInteractionMetadata(
            format = EventFormat.PROTOBUF,
            schema = descriptor.file.toProto().toString().trim(),
            dependencies = descriptor.file.dependencies.map { it.toProto().toString().trim() }
        )
    )
    block(event)
}
