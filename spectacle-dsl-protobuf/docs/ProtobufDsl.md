[//]: # ( {{ title: Protobuf DSL }} {{ tags: DSL, Interactions, Events }} {{ features: Interactions, Events  }} )

[//]: # ( {{ team: Spectacle Docs Writers }} )

# Protobuf DSL

The Protobuf DSL allows you to register your proto events as interactions in your tests.

Then all the events registered during your integration tests can be published to `Spectacle Central` and use a dedicated Events page to review all the
events in your system.

## How It Works

Once you create your event you just need to use some of these methods to registered:

* `consumesProtoEvent`
* `producesProtoEvent`

Example:

```kotlin
val event = SpecificationUpdatedProto
    .newBuilder()
    .setName("Spec1")
    .setComponent("TestService")
    .setDescription("Desc1")
    .setStatus(SpecStatusProto.IMPLEMENTED)
    .addSteps(SpecificationStepProto.newBuilder().setType(GIVEN).setDescription("step1").setIndex(1))
    .addSteps(SpecificationStepProto.newBuilder().setType(WHENEVER).setDescription("step2").setIndex(2))
    .build()
// It captures the metadata and schema for your event
consumesProtoEvent(event) {
    // Optional lambda to process your event
}
```
