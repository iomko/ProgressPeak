package com.practice.progress_peak.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var INSTANCE: ProgressPeakDatabase? = null

        fun getInstance(context: Context): ProgressPeakDatabase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ProgressPeakDatabase::class.java,
                    "progressPeak_db").build().also{
                    INSTANCE = it
                }
            }
        }
    }
}