package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features.SYSTEM_EVENTS
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.common.utils.api.getEventReport
import io.gianluigip.spectacle.common.utils.api.putSpecs
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.api.model.EventReportResponse
import io.gianluigip.spectacle.report.api.model.EventsReportResponse
import io.gianluigip.spectacle.report.api.model.ReportFiltersResponse
import io.gianluigip.spectacle.specification.api.model.FeatureToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationToUpdateRequest
import io.gianluigip.spectacle.specification.api.model.SpecificationsToUpdateRequest
import io.gianluigip.spectacle.specification.model.EventFormat
import io.gianluigip.spectacle.specification.model.EventFormat.JSON
import io.gianluigip.spectacle.specification.model.EventFormat.PROTOBUF
import io.gianluigip.spectacle.specification.model.EventInteractionMetadata
import io.gianluigip.spectacle.specification.model.InteractionDirection.INBOUND
import io.gianluigip.spectacle.specification.model.InteractionDirection.OUTBOUND
import io.gianluigip.spectacle.specification.model.InteractionType.EVENT
import io.gianluigip.spectacle.specification.model.SpecInteraction
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.SpecStatus.IMPLEMENTED
import org.junit.jupiter.api.Test

private const val SPEC_1 = "Spec1"
private const val SPEC_2 = "Spec2"
private const val SPEC_3 = "Spec3"
private const val FEATURE_1 = "Feature1"
private const val FEATURE_2 = "Feature2"
private const val TEAM_1 = "Team1"
private const val TEAM_2 = "Team2"
private const val SOURCE_1 = "Source1"
private const val SOURCE_2 = "Source2"
private const val COMPONENT_1 = "Component1"
private const val COMPONENT_2 = "Component2"
private const val TAG_1 = "Tag1"
private const val TAG_2 = "Tag2"

@Feature(
    name = SYSTEM_EVENTS, description = """
   Aggregate all the HTTP Interactions and generate the HTTP API for each service.  
"""
)
@SpecTags(Tags.SYSTEM_DIAGRAM, Tags.INTERACTIONS)
class EventsReportIT : BaseIntegrationTest() {

    @Test
    @Specification
    fun `Search the System Events by multiple parameters`() =
        given("existing Events interactions from multiple sources") {
            val source1 = SpecificationsToUpdateRequest(
                source = SOURCE_1, component = COMPONENT_1, features = listOf(
                    FeatureToUpdateRequest(
                        name = FEATURE_1, description = "",
                        specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_1, name = SPEC_1, status = IMPLEMENTED, tags = listOf(TAG_1), steps = listOf(),
                                interactions = listOf(
                                    SpecInteraction(OUTBOUND, EVENT, event1, metadata = eventMetadata1.toMap()),
                                    SpecInteraction(INBOUND, EVENT, event3, metadata = eventMetadata3.toMap()),
                                )
                            ),
                            SpecificationToUpdateRequest(
                                TEAM_1, name = SPEC_2, status = IMPLEMENTED, tags = listOf(TAG_1), steps = listOf(),
                                interactions = listOf(
                                    SpecInteraction(OUTBOUND, EVENT, event2, metadata = eventMetadata2.toMap()),
                                    SpecInteraction(OUTBOUND, EVENT, event1, metadata = eventMetadata1.toMap()),
                                )
                            )
                        )
                    ),
                )
            )
            val source2 = SpecificationsToUpdateRequest(
                source = SOURCE_2, component = COMPONENT_2, features = listOf(
                    FeatureToUpdateRequest(
                        name = FEATURE_2, description = "", specs = listOf(
                            SpecificationToUpdateRequest(
                                TEAM_2, name = SPEC_3, status = SpecStatus.NOT_IMPLEMENTED, tags = listOf(TAG_2), steps = listOf(),
                                interactions = listOf(SpecInteraction(OUTBOUND, EVENT, event3, metadata = eventMetadata3.toMap()))
                            )
                        )
                    ),
                )
            )
            putSpecs(source1)
            putSpecs(source2)
        } whenever "request a Events report for all the endpoints" run {
            getEventReport()
        } then "the report should contain all the events from all the sources" run { report ->
            report shouldBe EventsReportResponse(
                events = listOf(eventReport3, eventReport2, eventReport1),
                filters = filterAll
            )
        } andWhenever "search by parameters" then "the report filters the events accordingly" runAndFinish {
            getEventReport(source = SOURCE_1) shouldBe EventsReportResponse(
                events = listOf(eventReport3Source1, eventReport2, eventReport1), filters = filter1
            )
            getEventReport(feature = FEATURE_2) shouldBe EventsReportResponse(
                events = listOf(eventReport3Source2), filters = filter2
            )
            getEventReport(component = COMPONENT_1) shouldBe EventsReportResponse(
                events = listOf(eventReport3Source1, eventReport2, eventReport1), filters = filter1
            )
            getEventReport(tag = TAG_2) shouldBe EventsReportResponse(
                events = listOf(eventReport3Source2), filters = filter2
            )
            getEventReport(team = TEAM_1) shouldBe EventsReportResponse(
                events = listOf(eventReport3Source1, eventReport2, eventReport1), filters = filter1
            )
            getEventReport(eventName = "Deleted") shouldBe EventsReportResponse(
                events = listOf(eventReport3), filters = filterAll
            )
        }

    private val event1 = "SpecificationUpdatedProto"
    private val eventMetadata1 = EventInteractionMetadata(
        format = PROTOBUF,
        schema = "name: \"spectacle/SpecificationUpdated.proto\"\npackage: \"spectacle.events\"\ndependency: \"spectacle/SpecificationStatus.proto\"" +
                "\ndependency: \"spectacle/SpecificationStep.proto\"\nmessage_type {\n  name: \"SpecificationUpdatedProto\"\n  field {\n    name: " +
                "\"name\"\n    number: 1\n    label: LABEL_OPTIONAL\n    type: TYPE_STRING\n  }\n  field {\n    name: \"component\"\n    number: 2\n" +
                "    label: LABEL_OPTIONAL\n    type: TYPE_STRING\n  }\n  field {\n    name: \"description\"\n    number: 3\n    " +
                "label: LABEL_OPTIONAL\n    type: TYPE_STRING\n    oneof_index: 0\n    proto3_optional: true\n  }\n  field {\n    name: \"status\"\n" +
                "    number: 4\n    label: LABEL_OPTIONAL\n    type: TYPE_ENUM\n    type_name: \".spectacle.events.SpecStatusProto\"\n  }\n  field {" +
                "\n    name: \"steps\"\n    number: 5\n    label: LABEL_REPEATED\n    type: TYPE_MESSAGE\n    type_name: \"" +
                ".spectacle.events.SpecificationStepProto\"\n  }\n  oneof_decl {\n    name: \"_description\"\n  }\n}\noptions {\n  java_package: " +
                "\"io.gianluigip.spectacle.events\"\n  java_multiple_files: true\n}\nsyntax: \"proto3\"",
        dependencies = listOf(
            "name: \"spectacle/SpecificationStatus.proto\"\npackage: \"spectacle.events\"\nenum_type {\n  name: \"SpecStatusProto\"\n  value {\n    " +
                    "name: \"IMPLEMENTED\"\n    number: 0\n  }\n  value {\n    name: \"PARTIALLY_IMPLEMENTED\"\n    number: 1\n  }\n  value {\n    " +
                    "name: \"NOT_IMPLEMENTED\"\n    number: 2\n  }\n}\noptions {\n  java_package: \"io.gianluigip.spectacle.events\"\n  " +
                    "java_multiple_files: true\n}\nsyntax: \"proto3\"",
            "name: \"spectacle/SpecificationStep.proto\"\npackage: \"spectacle.events\"\nmessage_type {\n  name: \"SpecificationStepProto\"\n  field " +
                    "{\n    name: \"type\"\n    number: 1\n    label: LABEL_OPTIONAL\n    type: TYPE_ENUM\n    type_name: \"" +
                    ".spectacle.events.StepTypeProto\"\n  }\n  field {\n    name: \"description\"\n    number: 2\n    label: LABEL_OPTIONAL\n    " +
                    "type: TYPE_STRING\n  }\n  field {\n    name: \"index\"\n    number: 3\n    label: LABEL_OPTIONAL\n    type: TYPE_INT32\n  }\n}" +
                    "\nenum_type {\n  name: \"StepTypeProto\"\n  value {\n    name: \"GIVEN\"\n    number: 0\n  }\n  value {\n    name: \"WHENEVER\"" +
                    "\n    number: 1\n  }\n  value {\n    name: \"THEN\"\n    number: 2\n  }\n}\noptions {\n  java_package: \"" +
                    "io.gianluigip.spectacle.events\"\n  java_multiple_files: true\n}\nsyntax: \"proto3\""
        )
    )
    private val event2 = "SpecificationStepProto"
    private val eventMetadata2 = EventInteractionMetadata(
        format = PROTOBUF,
        schema = "name: \"spectacle/SpecificationStatus.proto\"\npackage: \"spectacle.events\"\nenum_type {\n  name: \"SpecStatusProto\"\n  value {\n" +
                "    name: \"IMPLEMENTED\"\n    number: 0\n  }\n  value {\n    name: \"PARTIALLY_IMPLEMENTED\"\n    number: 1\n  }\n  value {\n    " +
                "name: \"NOT_IMPLEMENTED\"\n    number: 2\n  }\n}\noptions {\n  java_package: \"io.gianluigip.spectacle.events\"\n  " +
                "java_multiple_files: true\n}\nsyntax: \"proto3\"",
        dependencies = listOf()
    )
    private val event3 = "SpecificationDeleted"
    private val eventMetadata3 = EventInteractionMetadata(
        format = EventFormat.JSON,
        schema = """
            {
                "specId": "3"
            }
        """.trimIndent(),
        dependencies = listOf()
    )

    private val eventReport1 = EventReportResponse(
        name = event1, producedBy = listOf(COMPONENT_1), consumedBy = emptyList(), format = PROTOBUF,
        schema = eventMetadata1.schema, dependencies = eventMetadata1.dependencies, features = listOf(FEATURE_1),
        sources = listOf(SOURCE_1), components = listOf(COMPONENT_1), tags = listOf(TAG_1), teams = listOf(TEAM_1),
    )
    private val eventReport2 = EventReportResponse(
        name = event2, producedBy = listOf(COMPONENT_1), consumedBy = emptyList(), format = PROTOBUF,
        schema = eventMetadata2.schema, dependencies = eventMetadata2.dependencies, features = listOf(FEATURE_1),
        sources = listOf(SOURCE_1), components = listOf(COMPONENT_1), tags = listOf(TAG_1), teams = listOf(TEAM_1),
    )
    private val eventReport3 = EventReportResponse(
        name = event3, producedBy = listOf(COMPONENT_2), consumedBy = listOf(COMPONENT_1), format = JSON,
        schema = eventMetadata3.schema, dependencies = eventMetadata3.dependencies, features = listOf(FEATURE_1, FEATURE_2),
        sources = listOf(SOURCE_1, SOURCE_2), components = listOf(COMPONENT_1, COMPONENT_2), tags = listOf(TAG_1, TAG_2),
        teams = listOf(TEAM_1, TEAM_2),
    )
    private val eventReport3Source1 = EventReportResponse(
        name = event3, producedBy = emptyList(), consumedBy = listOf(COMPONENT_1), format = JSON,
        schema = eventMetadata3.schema, dependencies = eventMetadata3.dependencies, features = listOf(FEATURE_1),
        sources = listOf(SOURCE_1), components = listOf(COMPONENT_1), tags = listOf(TAG_1), teams = listOf(TEAM_1),
    )
    private val eventReport3Source2 = EventReportResponse(
        name = event3, producedBy = listOf(COMPONENT_2), consumedBy = emptyList(), format = JSON,
        schema = eventMetadata3.schema, dependencies = eventMetadata3.dependencies, features = listOf(FEATURE_2),
        sources = listOf(SOURCE_2), components = listOf(COMPONENT_2), tags = listOf(TAG_2), teams = listOf(TEAM_2),
    )

    private val filterAll = ReportFiltersResponse(
        features = setOf(FEATURE_1, FEATURE_2),
        sources = setOf(SOURCE_1, SOURCE_2),
        components = setOf(COMPONENT_1, COMPONENT_2),
        tags = setOf(TAG_1, TAG_2),
        teams = setOf(TEAM_1, TEAM_2),
        statuses = emptySet(),
    )
    private val filter1 = ReportFiltersResponse(
        features = setOf(FEATURE_1),
        sources = setOf(SOURCE_1),
        components = setOf(COMPONENT_1),
        tags = setOf(TAG_1),
        teams = setOf(TEAM_1),
        statuses = emptySet(),
    )
    private val filter2 = ReportFiltersResponse(
        features = setOf(FEATURE_2),
        sources = setOf(SOURCE_2),
        components = setOf(COMPONENT_2),
        tags = setOf(TAG_2),
        teams = setOf(TEAM_2),
        statuses = emptySet(),
    )
}
