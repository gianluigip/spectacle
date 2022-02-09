package io.gianluigip.spectacle.common

class DummyTransactionExecutor : TransactionExecutor {
    override fun <T> execute(block: () -> T) = block.invoke()
}
