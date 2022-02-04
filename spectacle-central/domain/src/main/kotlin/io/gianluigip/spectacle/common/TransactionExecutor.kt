package io.gianluigip.spectacle.common

interface TransactionExecutor {

    fun <T> execute(block: () -> T): T

}
