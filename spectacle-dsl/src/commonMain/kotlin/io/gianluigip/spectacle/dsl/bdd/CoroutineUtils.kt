package io.gianluigip.spectacle.dsl.bdd

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * It allows running suspending functions in multiplatform tests.
 * It works similar than run blocking but with multiplatform support.
 * It's intended to be used only in tests.
 */
fun <R> suspendingInTest(block: suspend () -> R): R {
    val result: Optional<R> = Optional()
    val job = CoroutineScope(Dispatchers.Default).launch {
        result.value = block.invoke()
    }
    while (!job.isCompleted) {
        // It waits until the coroutine finishes
    }
    return result.value
}

private class Optional<T> {
    private val valueHolder: MutableList<T> = mutableListOf()
    var value: T
        get() = valueHolder.first()
        set(value) {
            valueHolder.clear()
            valueHolder.add(value)
        }
}
