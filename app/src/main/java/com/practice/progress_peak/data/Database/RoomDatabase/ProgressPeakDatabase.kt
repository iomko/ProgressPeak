package com.practice.progress_peak.data.Database.RoomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression
import com.practice.progress_peak.data.Database.Dao.ProgressPeakDao

//Spôsob vytvárania databázy bolo implementované za pomoci video tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
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