[//]: # ( {{ title: BDD DSL }} {{ tags: DSL, BDD }} {{ features: Specifications  }} )

[//]: # ( {{ team: Spectacle Docs Writers }} )

# BDD DSL

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
@Tags(Tag("Tag1")) // It supports Junit tags, beware Junit tags doesn't support whitespaces
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

If you are testing Spectacle with the default publisher `terminal` when your tests are executed you
will see:

```
Publishing Specifications:
Feature: Publisher Plugin
	DSL allow to write specs with minimum overhead
		Given a test with multiple steps
		Whenever it executes
		Then it should register all the BDD steps
```

`terminal` is just a basic publisher for testing before deciding to publish your specs
to `Spectacle Central`, you can use the config in the next section to enable the `central` publisher
and store your specs.
