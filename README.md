[![Maven Central](https://img.shields.io/maven-central/v/io.github.gianluigip/spectacle-dsl?label=Maven%20Central)](https://search.maven.org/artifact/io.github.gianluigip/spectacle-dsl)
[![Docker](https://img.shields.io/docker/v/gianluigipp/spectacle-central/latest?label=Docker)](https://hub.docker.com/r/gianluigipp/spectacle-central)

# Spectacle

### Living Documentation for Distributed Systems

In modern distributed systems the behavior of the product is defined across multiple components and
services, and usually developed by multiple teams, as a result, the documentation is fragmented and
difficult to find.

Additionally, the system tends to change faster than the documentation which makes the latter
unreliable.

Spectacle provides a solution for generating documentation from the multiple codebases in a modern
system and then storing it in a central repository that anyone can access.

It has two main components:

* `spectacle-dsl`: a Kotlin lib for transforming regular tests into specs that can be published to a
  central repository of specifications.
* `spectacle-central`: a repository for specifications that allow to find and share the behaviour of
  all the features in the system.

Spectacle follows the philosophy that only the code and by extension the tests provide accurate
documentation of the system, so it provides a DSL for writing readable tests that can be published
into a central repository to allows everyone to review.

## [Spectacle DSL](./spectacle-dsl/README.md)

A Kotlin multiplatform library for generating documentation from tests and Markdown files, it can be
used as a standalone tool for improving the readability of tests or in combination
with `Spectacle Central` to generate living documentation.

Add dependency:

```
testImplementation("io.github.gianluigip:spectacle-dsl:VERSION")
testImplementation("io.github.gianluigip:spectacle-dsl-assertions:VERSION")
```

Example of a JVM spec class:

```kotlin
@Feature("First Feature", description = "Description defining the feature.")
@ExtendWith(JUnitSpecificationReporter::class)
class JunitExampleTest {

    @Test
    @Specification
    fun `DSL allow to write specs with minimum overhead`() =
        given("a test with multiple steps") {
        } whenever "it executes" run {
        } then "it should register all the BDD steps" runAndFinish {
        }
}
```

Review [Spectacle DSL README](./spectacle-dsl/README.md) for more details.

## [Spectacle Central](./spectacle-central/README.md)

Repository for the living documentation generated by `Spectacle DSL`, it allows to centralize the
documentation generated from multiple executions of the `DSL` across multiple repositories, so even
though the docs are generated from multiple sources it can be queried from a central place which
increase the value for the organization as a whole.

![Specifications Page](./spectacle-central/docs/images/SpecificationsPage.png)

### [Live Demo](https://spectacle-central.herokuapp.com/)

* Username: `guest`
* Password: `guest`

Review [Spectacle Central README](./spectacle-central/README.md) for more details.

## Future Improvements

* A DSL for defining all the input and output dependencies that a spec has.

When writing integrations tests we usually mock events, queues and external
services, `spectacle-dsl` could provide a convenient way to register the mocks during the setup of
the tests, and later it can publish the dependencies to `spectacle-central` so it can build a graph
of all the dependencies in the system and how the services/modules interact with each other.

* Publish Markdown files from the repos running the `DSL` in `Central`.

Additionally to publish specs from tests, we can also publish local documents in Markdown, so we can
write the documentation close to the code and encourage updating in more frequently but at the same
time we can make it accessible for anyone in `Central`, providing the best balance between easy to
write and update and easy to review and search.
