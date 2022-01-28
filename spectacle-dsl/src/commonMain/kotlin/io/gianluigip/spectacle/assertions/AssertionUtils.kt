package io.gianluigip.spectacle.assertions

import kotlin.test.fail

fun runAndCatch(block: () -> Unit): Throwable {
    try {
        block.invoke()
        fail("Expected to catch an exception")
    } catch (throwable: Throwable) {
        return throwable
    }
}