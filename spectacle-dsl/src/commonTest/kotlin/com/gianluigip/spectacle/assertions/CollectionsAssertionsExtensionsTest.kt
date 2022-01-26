package com.gianluigip.spectacle.assertions

import com.gianluigip.spectacle.bdd.executeAndCatch
import kotlin.test.Test

class CollectionsAssertionsExtensionsTest {

    @Test
    fun `shouldHasSize should assert the collection size`() {
        listOf(1, 2) shouldHasSize 2
    }

    @Test
    fun `shouldHasSize should fail if the size is different`() =
        executeAndCatch {
            listOf(1, 2) shouldHasSize 3
        }.message shouldStartWith "Expected '[1, 2]' to has size 3"

    @Test
    fun `shouldBeEmpty should assert the collection is empty`() {
        listOf<Int>().shouldBeEmpty()
    }

    @Test
    fun `shouldBeEmpty should fail if it is not empty`() =
        executeAndCatch {
            listOf(1, 2).shouldBeEmpty()
        }.message shouldStartWith "Expected '[1, 2]' to be empty"

    @Test
    fun `shouldNotBeEmpty should assert the collection is not empty`() {
        listOf(1, 2).shouldNotBeEmpty()
    }

    @Test
    fun `shouldNotBeEmpty should fail if it is empty`() =
        executeAndCatch {
            listOf<Int>().shouldNotBeEmpty()
        }.message shouldStartWith "Expected '[]' to not be empty"

    @Test
    fun `shouldContainsAll should assert it contains all elements`() {
        listOf(1, 2) shouldContainsAll listOf(1, 2)
    }

    @Test
    fun `shouldContainsAll should fail if it not contains all elements`() =
        executeAndCatch {
            listOf(1, 2) shouldContainsAll listOf(1, 2, 3)
        }.message shouldStartWith "Expected '[1, 2]' to contains all elements in '[1, 2, 3]'"

    @Test
    fun `shouldContains should assert it contains an element`() {
        listOf(1, 2) shouldContains 1
    }

    @Test
    fun `shouldContains should fail if it not contains an element`() =
        executeAndCatch {
            listOf(1, 2) shouldContains 3
        }.message shouldStartWith "Expected '[1, 2]' to contains '3'"

    @Test
    fun `shouldNotContains should assert it not contains an element`() {
        listOf(1, 2) shouldNotContains 3
    }

    @Test
    fun `shouldNotContains should fail if it contains an element`() =
        executeAndCatch {
            listOf(1, 2) shouldNotContains 1
        }.message shouldStartWith "Expected '[1, 2]' to not contains '1'"
}