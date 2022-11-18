# HTTP DSL

The HTTP DSL is wrapper for the Ktor client that collects metadata about the calls that you make and is able to register them as spec interactions.

Then all the calls registered during your integration tests can be published to `Spectacle Central` and use a dedicated API page to review all the
HTTP endpoints in your system.

If you use this DSL in your integration tests you will be generating the API documentation for your service for free.

Add dependency:

```kotlin
  testImplementation("io.github.gianluigip:spectacle-dsl-http:VERSION")
```

You can review [how it works here](./docs/HttpDsl.md)
