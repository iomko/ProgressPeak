package com.practice.progress_peak.data.Database.RoomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression
import com.practice.progress_peak.data.Database.Dao.ProgressPeakDao

@Database(
    entities = [
        Habit::class,
        HabitProgression::class
               ],
    version = 1
)
@TypeConverters(RoomDataConverters::class)
abstract class ProgressPeakDatabase: RoomDatabase() {

    abstract val dao: ProgressPeakDao

}