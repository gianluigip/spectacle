package io.gianluigip.spectacle.common.utils

import java.time.ZonedDateTime
import kotlinx.datetime.Instant as KInstant

fun ZonedDateTime.toKotlinInstant(): KInstant =
    KInstant.fromEpochMilliseconds(toUtcInstant().toEpochMilli())
