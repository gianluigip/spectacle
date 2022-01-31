package io.gianluigip.spectacle.report

import io.gianluigip.spectacle.dsl.assertions.assertThat
import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.assertions.shouldBeNull
import io.gianluigip.spectacle.dsl.bdd.annotations.Feature
import io.gianluigip.spectacle.dsl.bdd.annotations.NotImplemented
import io.gianluigip.spectacle.dsl.bdd.annotations.PartiallyImplemented
import io.gianluigip.spectacle.dsl.bdd.annotations.SpecTags
import io.gianluigip.spectacle.dsl.bdd.annotations.Specification
import io.gianluigip.spectacle.dsl.bdd.annotations.Team
import io.gianluigip.spectacle.specification.SpecStatus
import org.junit.jupiter.api.Test

@Team("Team 1")
@Feature(
    "Feature 1", description = "Short Description"
)
@SpecTags("Tag3", "Tag4")
class MetadataExtractorTest {

    @Test
    fun `should extract metadata from annotations`() {
        val testMethod = MetadataExtractorTest::class.java.getMethod("implementedSpec")
        val metadata = MetadataExtractor.extract(testMethod, setOf("Tag1", "Tag2"))

        metadata!! assertThat {
            featureName shouldBe "Feature 1"
            featureDescription shouldBe "Short Description"
            team shouldBe "Team 1"
            status shouldBe SpecStatus.IMPLEMENTED
            tags shouldBe listOf("Tag1", "Tag2", "Tag3", "Tag4", "Tag5")
        }
    }

    @Test
    fun `should extract metadata from partially implemented spec`() {
        val testMethod = MetadataExtractorTest::class.java.getMethod("partiallyImplementedSpec")
        val metadata = MetadataExtractor.extract(testMethod, setOf())

        metadata!! assertThat {
            featureName shouldBe "Feature 1"
            featureDescription shouldBe "Short Description"
            team shouldBe "Team 1"
            status shouldBe SpecStatus.PARTIALLY_IMPLEMENTED
            tags shouldBe listOf("Tag3", "Tag4")
        }
    }

    @Test
    fun `should extract metadata from not implemented spec`() {
        val testMethod = MetadataExtractorTest::class.java.getMethod("notImplementedSpec")
        val metadata = MetadataExtractor.extract(testMethod, setOf())

        metadata!! assertThat {
            featureName shouldBe "Feature 1"
            featureDescription shouldBe "Short Description"
            team shouldBe "Team 1"
            status shouldBe SpecStatus.NOT_IMPLEMENTED
            tags shouldBe listOf("Tag3", "Tag4")
        }
    }

    @Test
    fun `should not extract metadata from no spec test`() {
        val testMethod = MetadataExtractorTest::class.java.getMethod("notSpec")
        val metadata = MetadataExtractor.extract(testMethod, setOf())
        metadata.shouldBeNull()
    }

    @Specification
    @SpecTags("Tag5")
    fun implementedSpec() {
    }

    @Specification
    @PartiallyImplemented
    fun partiallyImplementedSpec() {
    }

    @Specification
    @NotImplemented
    fun notImplementedSpec() {
    }

    fun notSpec() {
    }

}