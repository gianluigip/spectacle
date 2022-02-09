package io.gianluigip.spectacle.common.fixtures

import io.gianluigip.spectacle.specification.model.Component
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecStatus
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.SpecificationStep
import io.gianluigip.spectacle.specification.model.StepType.GIVEN
import io.gianluigip.spectacle.specification.model.TagName
import io.gianluigip.spectacle.specification.model.TeamName
import java.time.ZonedDateTime

object SpecConstants {
    val SPEC_NAME = SpecName("Spec1")
    val STATUS = SpecStatus.IMPLEMENTED
}

fun aSpec(
    name: SpecName = SpecConstants.SPEC_NAME,
    feature: FeatureName = FeatureConstants.FEATURE_NAME,
    team: TeamName = TeamConstants.TEAM_NAME,
    source: Source = FixtureConstants.SOURCE,
    component: Component = FixtureConstants.COMPONENT,
    status: SpecStatus = SpecConstants.STATUS,
    tags: List<TagName> = emptyList(),
    steps: List<SpecificationStep> = listOf(SpecificationStep(GIVEN, "Step1", 0)),
    creationTime: ZonedDateTime = FixtureConstants.CREATION_TIME,
    updateTime: ZonedDateTime = FixtureConstants.UPDATE_TIME,
) = Specification(name, feature, team, source, component, status, tags, steps, creationTime, updateTime)