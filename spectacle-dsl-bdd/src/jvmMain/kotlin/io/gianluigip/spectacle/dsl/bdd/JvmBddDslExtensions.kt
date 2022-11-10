package io.gianluigip.spectacle.dsl.bdd

import kotlinx.coroutines.runBlocking

@BddDslMarker
fun <R> coGiven(description: String, block: suspend (Unit) -> R): SpecificationBddWriter<R> = given(description) {
    runBlocking { block.invoke(it) }
}

@BddDslMarker
fun <R> coWhenever(description: String, block: suspend (Unit) -> R): SpecificationBddWriter<R> = whenever(description) {
    runBlocking { block.invoke(it) }
}

@BddDslMarker
infix fun <T, R> SpecificationBddWriter<T>.coRun(block: suspend (T) -> R): SpecificationBddWriter<R> = run {
    runBlocking { block.invoke(it) }
}

@BddDslMarker
infix fun <T> SpecificationBddWriter<T>.coRunAndFinish(block: suspend (T) -> Unit) = runAndFinish {
    runBlocking { block.invoke(it) }
}
