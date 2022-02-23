package io.gianluigip.spectacle.common.utils.fixtures

import io.gianluigip.spectacle.common.fixtures.FixtureConstants
import io.gianluigip.spectacle.common.fixtures.TeamConstants
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest

fun aWikiPageRequest(
    title: String = "Test Wiki",
    path: String = "/test",
    content: String = "# Test Wiki\n**Content**",
    checksum: String = "12345",
    team: String = TeamConstants.TEAM_NAME.value,
    tags: List<String> = listOf("Tag1", "Tag2"),
    features: List<String> = listOf("Feature1", "Feature2"),
    source: String = FixtureConstants.SOURCE.value,
    component: String = FixtureConstants.COMPONENT.value,
) = WikiPageRequest(
    title, path, content, checksum, team, tags, features, source, component
)