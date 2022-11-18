# Protobuf DSL

The Protobuf DSL allows you to register your proto events as interactions in your tests.

Then all the events registered during your integration tests can be published to `Spectacle Central` and use a dedicated Events page to review all the
events in your system.

Add dependency:

```kotlin
  testImplementation("io.github.gianluigip:spectacle-dsl-http:VERSION")
```

You can review [how it works here](./docs/ProtobufDsl.md)
