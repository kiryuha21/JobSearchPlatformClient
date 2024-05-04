package com.kiryuha21.jobsearchplatformclient.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toFormattedDateTime(formatter: DateTimeFormatter): String {
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    return dateTime.format(formatter)
}