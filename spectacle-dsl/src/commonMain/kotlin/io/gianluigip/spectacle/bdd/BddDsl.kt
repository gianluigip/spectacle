package io.gianluigip.spectacle.bdd

import kotlin.test.fail

fun <T, R> T.given(description: String, block: (T) -> R): R {
    return block.invoke(this)
}

infix fun <T> T.given(description: String): T {
    return this
}

infix fun <T> T.andGiven(description: String): T {
    return this
}

inline fun <T, R> T.whenever(description: String, block: (T) -> R): R {
    return block.invoke(this)
}

infix fun <T> T.whenever(description: String): T {
    return this
}

infix fun <T> T.andWhenever(description: String): T {
    return this
}

infix fun <T> T.then(description: String): T {
    return this
}

infix fun <T> T.andThen(description: String): T {
    return this
}

infix fun <T> T.and(description: String): T {
    return this
}

infix fun <T> T.validate(block: (T) -> Unit) {
    block.invoke(this)
}

infix fun <T, R> T.execute(block: (T) -> R): R {
    return block.invoke(this)
}

fun executeAndCatch(block: () -> Unit): Throwable {
    try {
        block.invoke()
        fail("Expected to catch an exception")
    } catch (throwable: Throwable) {
        return throwable
    }
}
