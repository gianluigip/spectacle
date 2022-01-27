# Spectacle

Spectacle is tool for writing acceptance tests / specifications in a microservice architecture.

It has two main components:

* `spectacle-dsl`: a Kotlin lib for transforming regular tests into specs that can be published to a
  central repository of specifications.
* `spectacle-central`(soon): a repository for specifications that allow to find and share the
  behaviour of all the features in the system.

Spectacle follows the philosophy that only the code and by extension the tests provide accurate
documentation of the system, so it provides a DSL for writing readable tests which can be
transformed into BDD statements that can be published into a central repository to allows anyone to
review.

## Spectacle DSL

Gradle:
```
implementation("io.github.gianluigip:spectacle-dsl:0.0.3")
```

### Future Improvements

* A DSL for defining all the input and output dependencies that a spec has. 
 
When writing integrations tests we usually mock events, queues and external services, `spectacle-dsl` could
provide a convenient way to register the mocks during the setup of the tests, and later it can
publish the dependencies to `spectacle-central` so it can build a graph of all the dependencies in
the system and how the services/modules interact with each other.
