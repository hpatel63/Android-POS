package com.polaris.hospitalitypos.core.data.local.converter

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class OffsetDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun fromString(value: String?): OffsetDateTime? = value?.let { OffsetDateTime.parse(it, formatter) }

    @TypeConverter
    fun toString(value: OffsetDateTime?): String? = value?.format(formatter)
}
