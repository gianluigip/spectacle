package io.gianluigip.spectacle.report

import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.exactly
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import io.gianluigip.spectacle.BaseIntegrationTest
import io.gianluigip.spectacle.common.Features
import io.gianluigip.spectacle.common.Tags
import io.gianluigip.spectacle.common.fixtures.aWikiMetadata
import io.gianluigip.spectacle.common.fixtures.aWikiRequest
import io.gianluigip.spectacle.common.utils.api.stubDeleteAnyWiki
import io.gianluigip.spectacle.common.utils.api.stubGetWiki
import io.gianluigip.spectacle.common.utils.api.stubPostAnyWiki
import io.gianluigip.spectacle.common.utils.api.stubPutAnyWiki
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.given
import io.gianluigip.spectacle.report.publisher.central.CentralWikiPublisher
import io.gianluigip.spectacle.report.publisher.central.getSHA256Hash
import io.gianluigip.spectacle.wiki.api.model.WikiPageRequest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

private val COMPLEX_CONTENT = """
    [//]: # ( {{ title: Wiki Publisher }} {{ features: Wiki, DSL }} )
    [//]: # ( {{ team: TestTeam }} {{ tags: Publisher, Test }} )

    # Wiki Publisher
""".trimIndent()

@Feature(Features.WIKI)
@SpecTags(Tags.WIKI)
class WikiPublisherIT : BaseIntegrationTest() {

    private val baseFolder = Files.createTempDirectory("wiki-it").toFile()
    private lateinit var docsFolder: File

    @Test
    @Specification
    fun `Can publish local wiki pages into Spectacle Central`() =
        given("a local docs folder with several markdowns files") {
            docsFolder = createFolder("docs", baseFolder)
            createFile("doc1.md", "# Doc 1", docsFolder)
            createFile("doc2.txt", "# Doc 2", docsFolder)
            createFile("doc3.md", COMPLEX_CONTENT, docsFolder)
            val dir1 = createFolder("dir1", docsFolder)
            createFile("doc4.md", "# Doc 4", dir1)
            val dir2 = createFolder("dir2", docsFolder)
            val dir3 = createFolder("dir3", dir2)
            createFile("doc5.md", "# Doc 5", dir3)
        } and "existing wiki pages in Central" run {
            stubGetWiki(
                response = listOf(
                    aWikiMetadata(id = "1", title = "doc1.md", fileName = "doc1.md", path = "/", checksum = getSHA256Hash("# Doc 1")),
                    aWikiMetadata(id = "3", title = "doc3.md", fileName = "doc3.md", path = "/", checksum = "CHANGED"),
                    aWikiMetadata(id = "9", title = "doc9.md", fileName = "doc9.md", path = "/", checksum = "TO DELETE"),
                )
            )
        } whenever "publish local wiki pages to Central" run {
            stubPostAnyWiki()
            stubPutAnyWiki("3")
            stubDeleteAnyWiki("9")
            runBlocking {
                CentralWikiPublisher.publishWiki(
                    reportConfiguration(centralWikiEnabled = true, localWikiLocation = docsFolder.absolutePath)
                )
            }
        } then "it should create, update or delete wiki pages as expected" runAndFinish {
            verifyWikiNotUpdated(
                aWikiRequest(title = "doc1.md", fileName = "doc1.md", path = "/", content = "# Doc 1"), wikiId = "1"
            )
            verifyWikiPageNotCreated(
                aWikiRequest(title = "doc2.txt", fileName = "doc2.txt", path = "/", content = "# Doc 2")
            )
            verifyWikiUpdated(
                aWikiRequest(
                    title = "Wiki Publisher", fileName = "doc3.md", path = "/", content = COMPLEX_CONTENT,
                    features = listOf("Wiki", "DSL"), tags = listOf("Publisher", "Test"), team = "TestTeam"
                ), wikiId = "3"
            )
            verifyWikiPageCreated(
                aWikiRequest(title = "doc4.md", fileName = "doc4.md", path = "/dir1", content = "# Doc 4")
            )
            verifyWikiPageCreated(
                aWikiRequest(title = "doc5.md", fileName = "doc5.md", path = "/dir2/dir3", content = "# Doc 5")
            )
            verifyWikiDeleted(wikiId = "9")
        }
}

private fun createFolder(name: String, parent: File): File =
    Files.createDirectories(Path("${parent.absolutePath}/$name")).toFile()

private fun createFile(fileName: String, content: String, folder: File): File =
    Files.createFile(Path("${folder.absolutePath}/${fileName}")).toFile().apply {
        Files.writeString(toPath(), content)
    }

private fun verifyWikiPageCreated(wikiPage: WikiPageRequest, times: Int = 1) {
    verify(
        exactly(times),
        postRequestedFor(urlEqualTo("/api/wiki"))
            .withRequestBody(equalToJson(Json.encodeToString(wikiPage)))
    )
}

private fun verifyWikiPageNotCreated(wikiPage: WikiPageRequest) = verifyWikiPageCreated(wikiPage, 0)

private fun verifyWikiUpdated(wikiPage: WikiPageRequest, wikiId: String, times: Int = 1) {
    verify(
        exactly(times),
        putRequestedFor(urlEqualTo("/api/wiki/$wikiId"))
            .withRequestBody(equalToJson(Json.encodeToString(wikiPage)))
    )
}

private fun verifyWikiNotUpdated(wikiPage: WikiPageRequest, wikiId: String, times: Int = 1) = verifyWikiUpdated(wikiPage, wikiId, 0)

private fun verifyWikiDeleted(wikiId: String) {
    verify(
        exactly(1),
        deleteRequestedFor(urlEqualTo("/api/wiki/$wikiId"))
    )
}
