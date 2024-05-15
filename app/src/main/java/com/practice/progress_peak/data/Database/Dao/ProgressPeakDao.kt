package com.practice.progress_peak.data.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ProgressPeakDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitProgression(habitProgression: HabitProgression): Long

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM Habit WHERE startDate <= :date AND endDate >= :date")
    fun getHabitsByDate(date: LocalDate): Flow<List<Habit>>

    @Query("SELECT * FROM Habit WHERE id = :id")
    suspend fun getHabitById(id: Int): Habit?

    @Query("SELECT id FROM Habit WHERE rowid = :rowId")
    suspend fun getHabitIdByRowId(rowId: Long): Int?

    @Query("SELECT * FROM HabitProgression WHERE habitId = :habitId AND date = :progressDate")
    suspend fun getHabitDateProgress(habitId: Int, progressDate: LocalDate): HabitProgression?

    @Query("SELECT * FROM HabitProgression WHERE id = :id")
    suspend fun getHabitProgressById(id: Int): HabitProgression?

    @Delete
    suspend fun deleteHabitProgress(habitProgress: HabitProgression)

    @Query("SELECT * FROM Habit")
    fun getAllHabits(): Flow<List<Habit>>
}