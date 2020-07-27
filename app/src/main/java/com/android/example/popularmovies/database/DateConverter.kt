package com.android.example.popularmovies.database

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}