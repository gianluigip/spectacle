package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.Test

class CollectionsAssertionsExtensionsTest {

    @Test
    fun `shouldHasSize_should_assert_the_collection_size`() {
        listOf(1, 2) shouldHasSize 2
    }

    @Test
    fun `shouldHasSize_should_fail_if_the_size_is_different`() =
        runAndCatch {
            listOf(1, 2) shouldHasSize 3
        }.message shouldStartWith "Expected '[1, 2]' to has size 3"

    @Test
    fun `shouldBeEmpty_should_assert_the_collection_is_empty`() {
        listOf<Int>().shouldBeEmpty()
    }

    @Test
    fun `shouldBeEmpty_should_fail_if_it_is_not_empty`() =
        runAndCatch {
            listOf(1, 2).shouldBeEmpty()
        }.message shouldStartWith "Expected '[1, 2]' to be empty"

    @Test
    fun `shouldNotBeEmpty_should_assert_the_collection_is_not_empty`() {
        listOf(1, 2).shouldNotBeEmpty()
    }

    @Test
    fun `shouldNotBeEmpty_should_fail_if_it_is_empty`() =
        runAndCatch {
            listOf<Int>().shouldNotBeEmpty()
        }.message shouldStartWith "Expected '[]' to not be empty"

    @Test
    fun `shouldContainsAll_should_assert_it_contains_all_elements`() {
        listOf(1, 2) shouldContainsAll listOf(1, 2)
    }

    @Test
    fun `shouldContainsAll_should_fail_if_it_not_contains_all_elements`() =
        runAndCatch {
            listOf(1, 2) shouldContainsAll listOf(1, 2, 3)
        }.message shouldStartWith "Expected '[1, 2]' to contains all elements in '[1, 2, 3]'"

    @Test
    fun `shouldContains_should_assert_it_contains_an_element`() {
        listOf(1, 2) shouldContains 1
    }

    @Test
    fun `shouldContains_should_fail_if_it_not_contains_an_element`() =
        runAndCatch {
            listOf(1, 2) shouldContains 3
        }.message shouldStartWith "Expected '[1, 2]' to contains '3'"

    @Test
    fun `shouldNotContains_should_assert_it_not_contains_an_element`() {
        listOf(1, 2) shouldNotContains 3
    }

    @Test
    fun `shouldNotContains_should_fail_if_it_contains_an_element`() =
        runAndCatch {
            listOf(1, 2) shouldNotContains 1
        }.message shouldStartWith "Expected '[1, 2]' to not contains '1'"
}