package io.gianluigip.spectacle.dsl.assertions

import kotlin.test.fail

@AssertionDslMarker
fun runAndCatch(block: () -> Unit): Throwable {
    try {
        block.invoke()
        fail("Expected to catch an exception")
    } catch (throwable: Throwable) {
        return throwable
    }
}