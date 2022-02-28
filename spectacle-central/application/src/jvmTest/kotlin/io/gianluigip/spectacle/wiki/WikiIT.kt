package io.gianluigip.spectacle.wiki

import io.gianluigip.spectacle.common.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.common.utils.CLOCK
import io.gianluigip.spectacle.common.utils.api.deleteWikiPage
import io.gianluigip.spectacle.common.utils.api.getWiki
import io.gianluigip.spectacle.common.utils.api.getWikiPage
import io.gianluigip.spectacle.common.utils.api.postWikiPage
import io.gianluigip.spectacle.common.utils.api.putWikiPage
import io.gianluigip.spectacle.common.utils.fixtures.aWikiPageRequest
import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.assertions.shouldHasSize
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.dsl.bdd.whenever
import org.junit.jupiter.api.Test

@Feature(
    name = Features.WIKI, description = """
    Spectacle Central can store the markdown files distributed across all the repos running Spectacle DSL, in that way the documentation can writen 
    near the code, encouraging writing and updating the documentation more often, and later you can use the Web App to conveniently review and share 
    the docs.
"""
)
@SpecTags(Tags.WIKI)
class WikiIT : BaseIntegrationTest() {

    private val pageCreationTime = CLOCK.millis()
    private lateinit var wikiId: String

    @Test
    @Specification
    fun `Manage Wiki Page lifecycle`() =
        whenever("save a new wiki page") {
            wikiId = postWikiPage(aWikiPageRequest()).id
        } then "it should get the page by its id" run {
            val expectedData = aWikiPageRequest()
            getWikiPage(wikiId)!! assertThat {
                id shouldBe wikiId
                title shouldBe expectedData.title
                path shouldBe expectedData.path
                fileName shouldBe expectedData.fileName
                content shouldBe expectedData.content
                checksum shouldBe expectedData.checksum
                team shouldBe expectedData.team
                tags shouldBe expectedData.tags
                features shouldBe expectedData.features
                source shouldBe expectedData.source
                component shouldBe expectedData.component
                creationTime.toEpochMilliseconds() shouldBe pageCreationTime
                updateTime.toEpochMilliseconds() shouldBe pageCreationTime
            }
        } andWhenever "it update the page" run {
            CLOCK.forwardMinutes(10)
            val pageToUpdate = aWikiPageRequest(
                title = "Test Wiki 2",
                path = "/test2",
                fileName = "test2.md",
                content = "contentChanged",
                checksum = "4321",
                team = "NewTeam",
                tags = listOf("Tag3"),
                features = listOf("Feature3"),
                source = "NewSource",
                component = "New Component",
            )
            putWikiPage(wikiId, pageToUpdate)
            pageToUpdate
        } then "the page should change" run { expectedData ->
            getWikiPage(wikiId)!! assertThat {
                id shouldBe wikiId
                title shouldBe expectedData.title
                path shouldBe expectedData.path
                fileName shouldBe expectedData.fileName
                content shouldBe expectedData.content
                checksum shouldBe expectedData.checksum
                team shouldBe expectedData.team
                tags shouldBe expectedData.tags
                features shouldBe expectedData.features
                source shouldBe expectedData.source
                component shouldBe expectedData.component
                creationTime.toEpochMilliseconds() shouldBe pageCreationTime
                updateTime.toEpochMilliseconds() shouldBe CLOCK.millis()
            }
        } andWhenever "the page is deleted" run {
            deleteWikiPage(wikiId)
        } then "the page can not be found" runAndFinish {
            getWikiPage(wikiId) shouldBe null
        }

    @Test
    @Specification
    fun `Search Wiki Page by parameter`() =
        given("several wiki pages") {
            postWikiPage(
                aWikiPageRequest(
                    title = "page1",
                    features = listOf("F1"),
                    source = "S1",
                    component = "C1",
                    tags = listOf("Tag1"),
                    team = "T1"
                )
            )
            postWikiPage(
                aWikiPageRequest(
                    title = "page2", features = listOf("F2"),
                    source = "S2",
                    component = "C2",
                    tags = listOf("Tag2"),
                    team = "T2"
                )
            )
        } whenever "search all the pages" run {
            getWiki()
        } then "it should return all the pages" run {
            it assertThat {
                shouldHasSize(2)
                first().title shouldBe "page1"
                last().title shouldBe "page2"
            }
        } andWhenever "search by feature" run {
            getWiki(feature = "F1")
        } then "it should only include pages from the expected feature" run {
            it assertThat {
                shouldHasSize(1)
                first().title shouldBe "page1"
            }
        } andWhenever "search by source" run {
            getWiki(source = "S2")
        } then "it should only include pages from the expected source" run {
            it assertThat {
                shouldHasSize(1)
                first().title shouldBe "page2"
            }
        } andWhenever "search by component" run {
            getWiki(component = "C1")
        } then "it should only include pages from the expected component" run {
            it assertThat {
                shouldHasSize(1)
                first().title shouldBe "page1"
            }
        } andWhenever "search by tag" run {
            getWiki(tag = "Tag2")
        } then "it should only include pages from the expected tag" run {
            it assertThat {
                shouldHasSize(1)
                first().title shouldBe "page2"
            }
        } andWhenever "search by team" run {
            getWiki(team = "T1")
        } then "it should only include pages from the expected team" runAndFinish {
            it assertThat {
                shouldHasSize(1)
                first().title shouldBe "page1"
            }
        }
}
