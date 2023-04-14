# Assertion DSL

To improve even further the readability Spectacle also include an Assertion DSL that can be used to
validate your tests more easily.

Add dependency:

```
testImplementation("io.github.gianluigip:spectacle-dsl-assertions:VERSION")
```

Example Test:

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
