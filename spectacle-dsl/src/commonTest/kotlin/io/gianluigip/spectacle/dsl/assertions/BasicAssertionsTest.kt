package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.Test

class BasicAssertionsTest {

    @Test
    fun `assertThat should execute inside the sut`() {
        "Test" assertThat {
            shouldBeNotNull()
            shouldBe("Test")
        }
    }

    @Test
    fun `shouldBe should assert equality`() {
        "Test" shouldBe "Test"
    }

    @Test
    fun `shouldBe should fail when not equal`() =
        runAndCatch {
            "Test" shouldBe "Test2"
        }.message shouldStartWith "Expected 'Test' to be equal to 'Test2'"

    @Test
    fun `shouldNotBe should assert inequality`() {
        1 shouldNotBe 2
    }

    @Test
    fun `shouldNotBe should fail when equal`() =
        runAndCatch {
            "Test" shouldNotBe "Test"
        }.message shouldStartWith "Expected 'Test' to be not equal to 'Test'"

    @Test
    fun `shouldBeNull should assert nullability`() {
        val result: String? = null
        result.shouldBeNull()
    }

    @Test
    fun `shouldBeNull should fail when not null`() =
        runAndCatch {
            "Test".shouldBeNull()
        }.message shouldStartWith "Expected 'Test' to be null"

    @Test
    fun `shouldBeNotNull should assert the value is not null`() {
        val result = "Test"
        result.shouldBeNotNull()
    }

    @Test
    fun `shouldBeNotNull should fail when null`() =
        runAndCatch {
            val result: String? = null
            result.shouldBeNotNull()
        }.message shouldStartWith "Expected 'null' to be not null"

    @Test
    fun `shouldBeInstanceOf should assert the instance type`() {
        "Test" shouldBeInstanceOf CharSequence::class
    }

    @Test
    fun `shouldBeInstanceOf should fail when the instance type is unexpected`() =
        runAndCatch {
            "Test" shouldBeInstanceOf Unit::class
        }.message shouldStartWith "Expected 'Test' to be instance of kotlin.Unit"
}