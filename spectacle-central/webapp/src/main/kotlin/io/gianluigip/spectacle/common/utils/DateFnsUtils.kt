@file:JsModule("date-fns")
@file:JsNonModule

package io.gianluigip.spectacle.common.utils

import io.gianluigip.spectacle.common.components.DateTimeFns

@JsName("getUnixTime")
external fun getUnixTime(date: DateTimeFns): Long

@JsName("parseISO")
external fun parseISO(date: String): DateTimeFns
