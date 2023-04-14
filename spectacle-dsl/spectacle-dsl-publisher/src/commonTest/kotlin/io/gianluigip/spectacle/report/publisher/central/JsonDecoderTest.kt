package io.gianluigip.spectacle.report.publisher.central

import io.gianluigip.spectacle.dsl.assertions.shouldBe
import io.gianluigip.spectacle.dsl.assertions.shouldHasSize
import kotlin.test.Test

class JsonDecoderTest {

    @Test
    fun decodeArrayOfStrings_should_extract_all_values() {
        """
            "t1"  , "t2,2","t3\",3\""
        """.trimIndent().decodeArrayOfStrings() shouldBe listOf(
            "t1", "t2,2", "t3\\\",3\\\""
        )
    }

    @Test
    fun decodeArrayOfStrings_should_extract_exactly_one_value() {
        """
            "t1"
        """.trimIndent().decodeArrayOfStrings() shouldBe listOf("t1")
    }

    @Test
    fun decodeArrayOfStrings_should_handle_zero_values() {
        """
        """.trimIndent().decodeArrayOfStrings() shouldBe emptyList()
    }

    @Test
    fun decodeArrayOfObjects_should_extract_all_values() {
        """
           [ { "k1":"v1" } , { "k2":"v2" },{"k3":"v3"} ]
        """.trimIndent().decodeArrayOfObjects() shouldBe listOf(
            "{ \"k1\":\"v1\" }", "{ \"k2\":\"v2\" }", "{\"k3\":\"v3\"}"
        )
    }

    @Test
    fun decodeArrayOfObjects_should_extract_exactly_one_value() {
        """
             [ { "k1":"v1" } ]
        """.trimIndent().decodeArrayOfObjects() shouldBe listOf("{ \"k1\":\"v1\" }")
    }

    @Test
    fun decodeArrayOfObjects_should_handle_zero_values() {
        """
            [  ]
        """.trimIndent().decodeArrayOfObjects() shouldHasSize 0
    }
}
