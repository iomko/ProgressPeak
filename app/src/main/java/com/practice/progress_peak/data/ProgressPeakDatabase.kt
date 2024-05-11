package com.practice.progress_peak.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practice.progress_peak.utils.Converters

@Database(
    entities = [Habit::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ProgressPeakDatabase: RoomDatabase() {

    abstract val dao: ProgressPeakDao

}