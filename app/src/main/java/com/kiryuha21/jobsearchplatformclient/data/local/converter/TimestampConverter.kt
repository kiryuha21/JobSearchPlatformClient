package com.kiryuha21.jobsearchplatformclient.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

class TimestampConverter {
    @TypeConverter
    fun timestampToLong(timestamp: LocalDateTime) : Long = timestamp.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun longToTimestamp(long: Long) : LocalDateTime = LocalDateTime.ofEpochSecond(long, 0, ZoneOffset.UTC)
}