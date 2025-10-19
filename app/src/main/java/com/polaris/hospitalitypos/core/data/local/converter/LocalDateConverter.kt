package com.polaris.hospitalitypos.core.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromString(value: String?): LocalDate? = value?.let(LocalDate::parse)

    @TypeConverter
    fun toString(value: LocalDate?): String? = value?.toString()
}
