package com.gianluigip.spectacle.assertions

import com.gianluigip.spectacle.bdd.executeAndCatch
import java.lang.RuntimeException
import kotlin.test.Test

class ThrowableAssertionsExtensionsTest {

    @Test
    fun `shouldBeCausedBy should assert the cause of a throwable`() {
        RuntimeException(IllegalArgumentException()) shouldBeCausedBy IllegalArgumentException::class
    }

    @Test
    fun `shouldBeCausedBy should fail if the cause is not as expected`() =
        executeAndCatch {
            RuntimeException(IllegalArgumentException()) shouldBeCausedBy IllegalStateException::class
        }.message shouldStartWith "Expected 'RuntimeException' to be caused by IllegalStateException"

}