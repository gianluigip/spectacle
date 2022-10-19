package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.wiki.api.model.WikiPageMetadataResponse
import kotlinx.datetime.Instant
import kotlin.test.Test

class WikiJsonEncoderTest {

    @Test
    fun decodeToWikiPage_should_parse_properly() {
        """
            {
              "id": "623e1f8f-1318-4844-87de-4df54afcf167",
              "title": "Assertion DSL",
              "path": "/Features",
              "fileName": "AssertionDsl.md",
              "checksum": "1df5cf082160d91c0e1fc3f1af848cdca934573d07428557a82db61801684e63",
              "team": "Spectacle",
              "tags": ["DSL", "Tag2"],
              "features": ["Specifications","Feature2"],
              "source": "spectacle-dsl",
              "component": "Spectacle DSL",
              "creationTime": "2022-02-28T10:49:53.407Z",
              "updateTime": "2022-02-28T10:49:53.407Z"
            }
        """.trimIndent().decodeToWikiPage() shouldBe WikiPageMetadataResponse(
            id = "623e1f8f-1318-4844-87de-4df54afcf167",
            title = "Assertion DSL",
            path = "/Features",
            fileName = "AssertionDsl.md",
            checksum = "1df5cf082160d91c0e1fc3f1af848cdca934573d07428557a82db61801684e63",
            team = "Spectacle",
            tags = listOf("DSL", "Tag2"),
            features = listOf("Specifications", "Feature2"),
            source = "spectacle-dsl",
            component = "Spectacle DSL",
            creationTime = Instant.parse("2022-02-28T10:49:53.407Z"),
            updateTime = Instant.parse("2022-02-28T10:49:53.407Z"),
        )
    }

}
