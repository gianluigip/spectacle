package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.Test

class BasicAssertionsTest {

    @Test
    fun assertThat_should_execute_inside_the_sut() {
        "Test" assertThat {
            shouldBeNotNull()
            shouldBe("Test")
        }
    }

    @Test
    fun shouldBe_should_assert_equality() {
        "Test" shouldBe "Test"
    }

    @Test
    fun shouldBe_should_fail_when_not_equal() =
        runAndCatch {
            "Test" shouldBe "Test2"
        }.message shouldStartWith "Expected 'Test' to be equal to 'Test2'"

    @Test
    fun shouldNotBe_should_assert_inequality() {
        1 shouldNotBe 2
    }

    @Test
    fun shouldNotBe_should_fail_when_equal() =
        runAndCatch {
            "Test" shouldNotBe "Test"
        }.message shouldStartWith "Expected 'Test' to be not equal to 'Test'"

    @Test
    fun shouldBeNull_should_assert_nullability() {
        val result: String? = null
        result.shouldBeNull()
    }

    @Test
    fun shouldBeNull_should_fail_when_not_null() =
        runAndCatch {
            "Test".shouldBeNull()
        }.message shouldStartWith "Expected 'Test' to be null"

    @Test
    fun shouldBeNotNull_should_assert_the_value_is_not_null() {
        val result = "Test"
        result.shouldBeNotNull()
    }

    @Test
    fun shouldBeNotNull_should_fail_when_null() =
        runAndCatch {
            val result: String? = null
            result.shouldBeNotNull()
        }.message shouldStartWith "Expected 'null' to be not null"

    @Test
    fun shouldBeInstanceOf_should_assert_the_instancetype() {
        "Test" shouldBeInstanceOf String::class
    }

    @Test
    fun shouldBeInstanceOf_should_fail_when_the_instance_type_is_unexpected() =
        runAndCatch {
            "Test" shouldBeInstanceOf Unit::class
        }.message shouldStartWith "Expected 'Test' to be instance of class "
}
