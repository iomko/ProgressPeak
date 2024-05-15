package com.practice.progress_peak.data.Database.RoomDatabase

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

//Spôsob vytvárania konvertorov pre databázu boli implementované podobným spôsobom ako z návodu:
//https://medium.com/@rasim0042/type-converter-for-room-db-2700e968a6d5
class RoomDataConverters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLong(localDate: LocalDate?): Long? {
        return localDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDate(value: Long?): LocalDate? {
        return value?.let {
            Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}