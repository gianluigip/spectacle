## Spectacle DSL

A Kotlin multiplatform library for writing readable tests as specifications, it includes an
Assertion DSL and a BDD DSL, optionally you can publish your specs into the specs
repository `Spectacle Central`.

Platforms supported:

* `Java`

## Getting Started

Add dependency:

```
testImplementation("io.github.gianluigip:spectacle-dsl:VERSION")
```

### BDD DSL

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

For this purpose Spectacle include a Junit plugin `JUnitSpecificationReporter` that allows to
publish your specifications, so we can generate living documentation from our tests.

```kotlin
import io.gianluigip.spectacle.dsl.bdd.annotations.*
import io.gianluigip.spectacle.dsl.bdd.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith

@Team("Spectacle Team") //It can be set globally using a config file
@Feature("Publisher Plugin", description = "Description defining the feature.")
@Tags(Tag("Tag1")) // It supports Junit tags, beware it Junit tags doesn't support whitespaces
@SpecTags("Tag2") // It supports whitespaces in the tag name 
@ExtendWith(JUnitSpecificationReporter::class) // It registers your specifications for publishing 
class JunitExampleTest {

    @Test
    @Specification // Only tests annotated as @Specification will be published
    @Tags(Tag("Tag3")) // You can mix functions and class tags
    @SpecTags("Tag4") // All tags annotations are optional
    fun `DSL allow to write specs with minimum overhead`() =
        // The name of the spec is the name of the function
        given("a test with multiple steps") {
        } whenever "it executes" run {
        } then "it should register all the BDD steps" runAndFinish {
        }
}
```

If you are testing Spectacle with the default publisher `terminal` you will see:

```
Publishing Specifications:
Feature: Publisher Plugin
	DSL allow to write specs with minimum overhead
		Given a test with multiple steps
		Whenever it executes
		Then it should register all the BDD steps
```

`terminal` is just a basic publisher for testing before deciding to publish your specs
to `Spectacle Central`.

#### Configure DSL

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
specification.publisher=terminal
```

Spectacle use the `source` to identify what specs are new and what specs were removed, so if you
reuse the same `source` for multiple tests executions like different services or several modules in
the same repo, you will overwrite the specs, each `spectacle.properties` should have a different
source.

## Assertion DSL

To improve even further the readability `Spectacle` also include an Assertion DSL that can be used
to validate your tests more easily.

```kotlin
import io.gianluigip.spectacle.dsl.assertions.*

data class User(val name: String, val roles: List<String>)

@Test
fun `validate user`() {
    val user = User(name = "John Doe", roles = listOf("Admin", "Sales"))
    // All assertions are extension functions starting with `should`
    user.shouldBeNull()
    // assertThat is a convenient method for doing multiple validations on the same instance
    // Work similar to `run` but with infix syntax
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
