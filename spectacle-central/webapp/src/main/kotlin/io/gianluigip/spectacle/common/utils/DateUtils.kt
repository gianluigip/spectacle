package io.gianluigip.spectacle.common.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toDisplay(): String = toLocalDateTime(TimeZone.currentSystemDefault()).run {
    "${dayOfMonth.fillZeros()}/${monthNumber.fillZeros()}/$year ${hour.fillZeros()}:${minute.fillZeros()}:${second.fillZeros()}"
}

private fun Int.fillZeros(): String {
    return if (this < 10) "0$this" else this.toString()
}
