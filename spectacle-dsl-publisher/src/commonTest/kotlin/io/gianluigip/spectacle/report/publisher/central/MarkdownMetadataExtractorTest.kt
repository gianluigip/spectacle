package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.dsl.assertions.shouldBe
import kotlin.test.Test

private val exampleContent = """
    [//]: # ( {{ title: BDD DSL }} {{ tags: DSL, BDD }} {{ features: Specifications  }} )

    [//]: # ( {{ team: Spectacle Docs Writers }} {{ title: BDD DSL 2 }} )

    # BDD DSL
""".trimIndent()

class MarkdownMetadataExtractorTest {

    @Test
    fun extractTitleFromWikiPage_should_extract_title() {
        exampleContent.extractTitleFromWikiPage() shouldBe "BDD DSL"
    }

    @Test
    fun extractTitleFromWikiPage_should_detect_some_special_chars() {
        """
            [//]: # ( {{ title: [Test] Title - Sub,Title/Sub.Header }} )
        """.trimIndent().extractTitleFromWikiPage() shouldBe "[Test] Title - Sub,Title/Sub.Header"
    }

    @Test
    fun extractTeamFromWikiPage_should_extract_team() {
        exampleContent.extractTeamFromWikiPage() shouldBe "Spectacle Docs Writers"
    }

    @Test
    fun extractFeatureFromWikiPage_should_extract_feature() {
        exampleContent.extractFeaturesFromWikiPage() shouldBe listOf("Specifications")
    }

    @Test
    fun extractFeatureFromWikiPage_should_extract_multiple_features() {
        """
            {{ features: Feature1, Complex Feature, F3  }}
        """.trimIndent().extractFeaturesFromWikiPage() shouldBe listOf("Feature1", "Complex Feature", "F3")
    }

    @Test
    fun extractTagsFromWikiPage_should_extract_tags() {
        exampleContent.extractTagsFromWikiPage() shouldBe listOf("DSL", "BDD")
    }

    @Test
    fun extractTagsFromWikiPage_should_return_empty_if_not_present() {
        "exampleContent".extractTagsFromWikiPage() shouldBe emptyList()
    }
}