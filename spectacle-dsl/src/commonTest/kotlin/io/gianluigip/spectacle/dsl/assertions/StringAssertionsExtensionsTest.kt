package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.Test

class StringAssertionsExtensionsTest {

    @Test
    fun `shouldStartWith should assert that starts with the right text`() {
        "Hello world" shouldStartWith "Hello"
    }

    @Test
    fun `shouldStartWith should fail if it doesn't start with the right text`() =
        runAndCatch {
            "Hello world" shouldStartWith "Helo"
        }.message shouldStartWith "Expected 'Hello world' to start with 'Helo'"

    @Test
    fun `shouldEndWith should assert that ends with the right text`() {
        "Hello world" shouldEndWith "world"
    }

    @Test
    fun `shouldEndWith should fail if it doesn't end with the right text`() =
        runAndCatch {
            "Hello world" shouldEndWith "world!!"
        }.message shouldStartWith "Expected 'Hello world' to end with 'world!!'"

    @Test
    fun `shouldContains should assert that contains the right text`() {
        "Hello world" shouldContains "llo wo"
    }

    @Test
    fun `shouldContains should fail if it doesn't contains the right text`() =
        runAndCatch {
            "Hello world" shouldContains "abc"
        }.message shouldStartWith "Expected 'Hello world' to contains 'abc'"

    @Test
    fun `shouldNotBeEmpty should assert that the text is not empty`() {
        "Hello world".shouldNotBeEmpty()
    }

    @Test
    fun `shouldNotBeEmpty should fail if it's empty'`() =
        runAndCatch {
            "".shouldNotBeEmpty()
        }.message shouldStartWith "Expected '' to be not empty"

    @Test
    fun `shouldBeEmpty should assert that the text is not empty`() {
        "".shouldBeEmpty()
    }

    @Test
    fun `shouldBeEmpty should fail if it's not empty'`() =
        runAndCatch {
            "Hello world".shouldBeEmpty()
        }.message shouldStartWith "Expected 'Hello world' to be empty"
}