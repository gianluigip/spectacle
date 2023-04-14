package io.gianluigip.spectacle.dsl.interactions

import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.whenever
import io.gianluigip.spectacle.events.SpecStatusProto
import io.gianluigip.spectacle.events.SpecificationStepProto
import io.gianluigip.spectacle.events.SpecificationUpdatedProto
import io.gianluigip.spectacle.events.StepTypeProto
import io.gianluigip.spectacle.report.junit.JUnitSpecificationReporter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Feature(Features.SYSTEM_EVENTS)
@SpecTags(Tags.PROTOBUF, Tags.INTERACTIONS, "Fake Data")
@ExtendWith(JUnitSpecificationReporter::class)
class ProtoExampleInteractionsTest {

    @Test
    @Specification
    fun `The Protobuf Interactions DSL can publish events schemas to Central`() =
        whenever("proto events are consumed or published") {
            val event1 = SpecificationUpdatedProto
                .newBuilder()
                .setName("Spec1")
                .setComponent("TestService")
                .setDescription("Desc1")
                .setStatus(SpecStatusProto.IMPLEMENTED)
                .addSteps(SpecificationStepProto.newBuilder().setType(StepTypeProto.GIVEN).setDescription("step1").setIndex(1))
                .addSteps(SpecificationStepProto.newBuilder().setType(StepTypeProto.WHENEVER).setDescription("step2").setIndex(2))
                .build()
            val event2 = SpecificationStepProto.newBuilder().setType(StepTypeProto.GIVEN).setDescription("step1").setIndex(1).build()

            consumesProtoEvent(event1)
            producesProtoEvent(event1)
            consumesProtoEvent(event2)
        } then "their schema and metadata are registered and published to Central" runAndFinish {
        }

}
