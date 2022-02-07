package io.gianluigip.spectacle.common

import org.jetbrains.exposed.sql.transactions.transaction

class ExposedTransactionExecutor : TransactionExecutor {
    override fun <T> execute(block: () -> T): T = transaction {
        block.invoke()
    }
}