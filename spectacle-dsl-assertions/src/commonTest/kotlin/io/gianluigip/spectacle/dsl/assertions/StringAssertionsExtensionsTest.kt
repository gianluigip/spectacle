package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.Test

class StringAssertionsExtensionsTest {

    @Test
    fun shouldStartWith_should_assert_that_starts_with_the_right_text() {
        "Hello world" shouldStartWith "Hello"
    }

    @Test
    fun shouldStartWith_should_fail_if_it_doesnt_start_with_the_right_text() =
        runAndCatch {
            "Hello world" shouldStartWith "Helo"
        }.message shouldStartWith "Expected 'Hello world' to start with 'Helo'"

    @Test
    fun shouldEndWith_should_assert_that_ends_with_the_right_text() {
        "Hello world" shouldEndWith "world"
    }

    @Test
    fun shouldEndWith_should_fail_if_it_doesnt_end_with_the_right_text() =
        runAndCatch {
            "Hello world" shouldEndWith "world!!"
        }.message shouldStartWith "Expected 'Hello world' to end with 'world!!'"

    @Test
    fun shouldContains_should_assert_that_contains_the_right_text() {
        "Hello world" shouldContains "llo wo"
    }

    @Test
    fun shouldContains_should_fail_if_it_doesnt_contains_the_right_text() =
        runAndCatch {
            "Hello world" shouldContains "abc"
        }.message shouldStartWith "Expected 'Hello world' to contains 'abc'"

    @Test
    fun shouldNotBeEmpty_should_assert_that_the_text_is_not_empty() {
        "Hello world".shouldNotBeEmpty()
    }

    @Test
    fun shouldNotBeEmpty_should_fail_if_its_empty() =
        runAndCatch {
            "".shouldNotBeEmpty()
        }.message shouldStartWith "Expected '' to be not empty"

    @Test
    fun shouldBeEmpty_should_assert_that_the_text_is_not_empty() {
        "".shouldBeEmpty()
    }

    @Test
    fun shouldBeEmpty_should_fail_if_its_not_empty() =
        runAndCatch {
            "Hello world".shouldBeEmpty()
        }.message shouldStartWith "Expected 'Hello world' to be empty"
}