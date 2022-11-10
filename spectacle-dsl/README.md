[![Maven Central](https://img.shields.io/maven-central/v/io.github.gianluigip/spectacle-dsl?label=Maven%20Central)](https://search.maven.org/artifact/io.github.gianluigip/spectacle-dsl)

# [Spectacle DSL](./docs/Overview.md)

A Kotlin multiplatform library for writing readable tests as specifications, it includes an
Assertion DSL and a BDD DSL, optionally you can publish your specs into the specs
repository `Spectacle Central`.

Platforms supported:

* `Java`
* `JS` - Limited, it can not publish specs yet.

## Getting Started

Add dependency:

```
testImplementation("io.github.gianluigip:spectacle-dsl:VERSION")
```

### [BDD DSL](docs/Features/BddDsl.md)

The simpler use case is to use the DSL to make your tests more readable, it's particularly useful
for writing Integration Tests because it usually validates complex behaviour and requires more setup
and preconditions.

```kotlin
@Test
fun `DSL allow to write specs with minimum overhead`() =
    given("a test with multiple steps") {
    } whenever "it executes" run {
    } then "it should register all the BDD steps" runAndFinish {
    }
```

Now you created a test that is easy to read and other devs can rely on it for understanding your
software, but we can do it better, we can extract all the BDD steps along with some extra metadata
to build a specification that can be shared outside the codebase.

You can read more about how to publish your specs in the [BDD DSL Page](docs/Features/BddDsl.md).

## [Configure DSL](./docs/ConfigureDsl.md)

To change the default behaviour when publishing you need to add `spectacle.properties` to your
test `resources` folder with the following content:

```properties
# Name of the main team maintaining the code
# You don't need to use @Team if the name is the same
specification.team=Spectacle
# Unique identifier for your Gradle module
# If you repeat the name you can overwrite specs when using some publishers
specification.source=spectacle-dsl
# Name of the component/service, it can be repeated
# Multiple sources (modules) are common for one service
specification.component=Spectacle DSL
# List separated by comma of publishers that you want to use
specification.publisher=terminal,central
# Central is the main publisher that allow to centralize all the specs in one place
# These properties can be also defined as env vars, so it's easier to use in CI
# Can be overwritten with the env var SPECIFICATION_PUBLISHER_CENTRAL_ENABLED
specification.publisher.central.enabled=false
# Can be overwritten with the env var SPECIFICATION_PUBLISHER_CENTRAL_HOST
specification.publisher.central.host=https://central.spectacle.com
# If this property is not set the central publisher will fail because it won't have the credentials to publish changes.
# Can be overwritten with the env var SPECIFICATION_PUBLISHER_CENTRAL_USERNAME
specification.publisher.central.username=admin
# Can be overwritten with the env var SPECIFICATION_PUBLISHER_CENTRAL_PASSWORD
specification.publisher.central.password=admin
# Don't publish to Central if there wasn't any spec registered, publish to Central without specs delete all the existing specs for that source.
specification.publisher.central.publish-empty-specs=false
# You can publish your Markdown files to Spectacle Central
specification.publisher.central.wiki.enabled=true
# If you are publishing your Markdown docs you need to define which folder will be published
specification.publisher.central.wiki.localFolderLocation=docs
```

Spectacle use the `source` to identify what specs are new and what specs were removed, so if you
reuse the same `source` for multiple tests executions like different services or several modules in
the same repo, you will overwrite the specs, each `spectacle.properties` should have a different
source.

## [Assertion DSL](docs/Features/AssertionDsl.md)

To improve even further the readability Spectacle also include an Assertion DSL that can be used to
validate your tests more easily.

```kotlin
import io.gianluigip.spectacle.dsl.assertions.*

data class User(val name: String, val roles: List<String>)

@Test
fun `validate user`() {
    val user = User(name = "John Doe", roles = listOf("Admin", "Sales"))
    // All assertions are extension functions starting with `should`
    user.shouldBeNull()
    // assertThat is a convenient method for doing multiple validations on the same instance
    // It works similar to `run` but with infix syntax
    user assertThat {
        // Most assertions support infix syntax so the validations reads as normal sentences 
        name shouldStartWith "John"
        name shouldEndWith "Doe"
        roles shouldHasSize 2
        roles shouldContains "Admin"
    }
}
```

It relies on extension functions and infix to provide a clear syntax that reduce noise and
boilerplate in your tests.

The Assertion DSL can be use independently although is intended to be use along the BDD DSL to
maximize the readability of your specifications.

## [Interaction DSL](./docs/Features/InteractionsDsl.md)

When writing your specs with the `BDD DSL` you can optionally also use the `Interactions DSL` to
register what dependencies you are setting up or what is the output of your component.

If you add interactions to your specs then it will be published to `Central` along with he specs and
the tool will be able to generate a system diagram that shows the interactions between all the
components and teams.

For more information you can visit the [Interaction DSL Page](./docs/Features/InteractionsDsl.md)

# [Wiki](./docs/Features/Wiki.md)

Additionally to the writing and publishing of specs, `Spectacle DSL` can find all the Markdown files
in a given folder and sync it then in `Spectacke Central`, the idea is that you can write your tech
docs close to your code but search it in a central place that any team member or stakeholder can
quickle reference.

For more information you can visit the [Wiki Page](./docs/Features/Wiki.md)
