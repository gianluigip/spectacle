[//]: # ( {{ title: Interactions DSL }} {{ features: System Diagram }} )

# Interactions DSL

When writing your specs with the `BDD DSL` you can optionally also use the `Interactions DSL` to
register what dependencies you are setting up or what is the output of your component.

The DSL support registering interactions for `Events`, `HTTP Calls` and `Persistence`.

### Events Interactions

You can register all the events that your component consume and produce with the following
functions:

```kotlin
// It can be used in your tests when simulating the reception of a new event.
consumesEvent("SpecCreatedEvent")

// It can be used in your tests when verifying your code published an event.
producesEvent("SpecCreatedEvent")
```

### HTTP Interactions

You can register what components do you expect to use your code and what components are being used
by your code.

```kotlin
// spectacle-central is simulating the reception of a request from spectacle-dsl
receivesRequestFrom("Spectacle DSL")

// spectacle-dsl is mocking the request to spectacle-central
sendsRequestTo("Spectacle Central")
```

### Persistence Interactions

You could have multiple persistence services in your system, like a relational database, an external
cache or a non sql database.

```kotlin
usesPersistence("Spectacle DB")
usesPersistence("Redis Spec Cache")
```

## Benefits

If you add interactions to your specs then it will be published to `Central` along with he specs and
the tool will be able to generate a system diagram that shows the interactions between all the
components and teams. 
