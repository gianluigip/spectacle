package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.bdd.TestContext
import io.gianluigip.spectacle.dsl.bdd.aSpec
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.coRun
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.events.SpecStatusProto
import io.gianluigip.spectacle.events.SpecificationStepProto
import io.gianluigip.spectacle.events.SpecificationUpdatedProto
import io.gianluigip.spectacle.events.StepTypeProto.GIVEN
import io.gianluigip.spectacle.events.StepTypeProto.WHENEVER
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import io.gianluigip.spectacle.specification.model.InteractionDirection
import io.gianluigip.spectacle.specification.model.InteractionType
import io.gianluigip.spectacle.specification.model.SpecInteraction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Feature(Features.SYSTEM_EVENTS)
@SpecTags(Tags.PROTOBUF, Tags.INTERACTIONS)
@ExtendWith(JUnitSpecificationReporter::class)
class ProtoInteractionsDslTest {

    @Test
    @Specification
    fun `The Protobuf Interactions DSL can register proto events and generate documentation from it`() {
        val spec = given("an event") {
        } whenever "register consuming it" run {
        } then "the event was registered as an interaction with comprehensive documentation" run {

            val testSpec = aSpec() whenever "run" coRun {
                val event = SpecificationUpdatedProto
                    .newBuilder()
                    .setName("Spec1")
                    .setComponent("TestService")
                    .setDescription("Desc1")
                    .setStatus(SpecStatusProto.IMPLEMENTED)
                    .addSteps(SpecificationStepProto.newBuilder().setType(GIVEN).setDescription("step1").setIndex(1))
                    .addSteps(SpecificationStepProto.newBuilder().setType(WHENEVER).setDescription("step2").setIndex(2))
                    .build()
                consumesProtoEvent(event) {
                    // Optional lambda to process your event
                }
            }
            testSpec.specBuilder.build().interactions.first() shouldBe SpecInteraction(
                direction = InteractionDirection.INBOUND,
                type = InteractionType.EVENT,
                name = "SpecificationUpdatedProto",
                metadata = mapOf(
                    "metadataType" to "EVENT",
                    "format" to "PROTOBUF",
                    "schema" to """
                        name: "spectacle/SpecificationUpdated.proto"
                        package: "spectacle.events"
                        dependency: "spectacle/SpecificationStatus.proto"
                        dependency: "spectacle/SpecificationStep.proto"
                        message_type {
                          name: "SpecificationUpdatedProto"
                          field {
                            name: "name"
                            number: 1
                            label: LABEL_OPTIONAL
                            type: TYPE_STRING
                          }
                          field {
                            name: "component"
                            number: 2
                            label: LABEL_OPTIONAL
                            type: TYPE_STRING
                          }
                          field {
                            name: "description"
                            number: 3
                            label: LABEL_OPTIONAL
                            type: TYPE_STRING
                            oneof_index: 0
                            proto3_optional: true
                          }
                          field {
                            name: "status"
                            number: 4
                            label: LABEL_OPTIONAL
                            type: TYPE_ENUM
                            type_name: ".spectacle.events.SpecStatusProto"
                          }
                          field {
                            name: "steps"
                            number: 5
                            label: LABEL_REPEATED
                            type: TYPE_MESSAGE
                            type_name: ".spectacle.events.SpecificationStepProto"
                          }
                          oneof_decl {
                            name: "_description"
                          }
                        }
                        options {
                          java_package: "io.gianluigip.spectacle.events"
                          java_multiple_files: true
                        }
                        syntax: "proto3"
                        """.trimIndent(),
                    "dependency__0" to """
                        name: "spectacle/SpecificationStatus.proto"
                        package: "spectacle.events"
                        enum_type {
                          name: "SpecStatusProto"
                          value {
                            name: "IMPLEMENTED"
                            number: 0
                          }
                          value {
                            name: "PARTIALLY_IMPLEMENTED"
                            number: 1
                          }
                          value {
                            name: "NOT_IMPLEMENTED"
                            number: 2
                          }
                        }
                        options {
                          java_package: "io.gianluigip.spectacle.events"
                          java_multiple_files: true
                        }
                        syntax: "proto3"
                        """.trimIndent(),
                    "dependency__1" to """
                        name: "spectacle/SpecificationStep.proto"
                        package: "spectacle.events"
                        message_type {
                          name: "SpecificationStepProto"
                          field {
                            name: "type"
                            number: 1
                            label: LABEL_OPTIONAL
                            type: TYPE_ENUM
                            type_name: ".spectacle.events.StepTypeProto"
                          }
                          field {
                            name: "description"
                            number: 2
                            label: LABEL_OPTIONAL
                            type: TYPE_STRING
                          }
                          field {
                            name: "index"
                            number: 3
                            label: LABEL_OPTIONAL
                            type: TYPE_INT32
                          }
                        }
                        enum_type {
                          name: "StepTypeProto"
                          value {
                            name: "GIVEN"
                            number: 0
                          }
                          value {
                            name: "WHENEVER"
                            number: 1
                          }
                          value {
                            name: "THEN"
                            number: 2
                          }
                        }
                        options {
                          java_package: "io.gianluigip.spectacle.events"
                          java_multiple_files: true
                        }
                        syntax: "proto3"
                        """.trimIndent()

                )
            )
        }

        TestContext.setCurrentSpec(spec.specBuilder)
    }

}
