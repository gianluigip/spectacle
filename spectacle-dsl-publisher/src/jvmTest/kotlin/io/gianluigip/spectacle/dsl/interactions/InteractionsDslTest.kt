package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags.SYSTEM_DIAGRAM
import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.dsl.bdd.aSpec
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.InteractionType.HTTP
import io.gianluigip.spectacle.specification.model.InteractionType.PERSISTENCE
import io.gianluigip.spectacle.specification.model.SpecInteraction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Feature(
    name = Features.SYSTEM_DIAGRAM,
    description = """
        While writing your specs you can register the interactions it has, like what events, requests or database your test is mocking or simulating.
        So Central can use that data to generate a diagram of all the interactions in the system.
        """
)
@SpecTags(SYSTEM_DIAGRAM)
@ExtendWith(JUnitSpecificationReporter::class)
class InteractionsDslTest {

    @Test
    @Specification
    fun `Interactions DSL can register events consumed`() {
        val spec = given("a testable spec") {
            aSpec("Test Spec")
        } whenever "it simulate consuming an event" run {
            consumesEvent("TestEvent")
        } then "it should register the event consumed" run {
            TestContext.getCurrentSpec()!!.build() assertThat {
                interactions shouldBe listOf(
                    SpecInteraction(INBOUND, EVENT, "TestEvent")
                )
            }
        }
        TestContext.setCurrentSpec(spec.specBuilder)
    }

    @Test
    @Specification
    fun `Interactions DSL can register events produced`() {
        val spec = given("a testable spec") {
            aSpec("Test Spec")
        } whenever "it simulate producing an event" run {
            producesEvent("TestEvent")
        } then "it should register the event produced" run {
            TestContext.getCurrentSpec()!!.build() assertThat {
                interactions shouldBe listOf(
                    SpecInteraction(OUTBOUND, EVENT, "TestEvent")
                )
            }
        }
        TestContext.setCurrentSpec(spec.specBuilder)
    }

    @Test
    @Specification
    fun `Interactions DSL can register request received`() {
        val spec = given("a testable spec") {
            aSpec("Test Spec")
        } whenever "it simulate receiving a request" run {
            receivesRequestFrom("TestService")
        } then "it should register the component that do the request" run {
            TestContext.getCurrentSpec()!!.build() assertThat {
                interactions shouldBe listOf(
                    SpecInteraction(INBOUND, HTTP, "TestService")
                )
            }
        }
        TestContext.setCurrentSpec(spec.specBuilder)
    }

    @Test
    @Specification
    fun `Interactions DSL can register request send`() {
        val spec = given("a testable spec") {
            aSpec("Test Spec")
        } whenever "it simulate sending a request" run {
            sendsRequestTo("TestService")
        } then "it should register the component that receive the request send" run {
            TestContext.getCurrentSpec()!!.build() assertThat {
                interactions shouldBe listOf(
                    SpecInteraction(OUTBOUND, HTTP, "TestService")
                )
            }
        }
        TestContext.setCurrentSpec(spec.specBuilder)
    }

    @Test
    @Specification
    fun `Interactions DSL can register persistence used`() {
        val spec = given("a testable spec") {
            aSpec("Test Spec")
        } whenever "it simulate using persistence" run {
            usesPersistence("TestDB")
        } then "it should register the database used" run {
            TestContext.getCurrentSpec()!!.build() assertThat {
                interactions shouldBe listOf(
                    SpecInteraction(OUTBOUND, PERSISTENCE, "TestDB")
                )
            }
        }
        TestContext.setCurrentSpec(spec.specBuilder)
    }
}
