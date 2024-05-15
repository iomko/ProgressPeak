package com.practice.progress_peak.data


import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ProgressPeakRepository {

    suspend fun insertHabit(habit: Habit): Long

    suspend fun insertHabitProgression(habitProgression: HabitProgression): Long

    suspend fun deleteHabit(habit: Habit)

    suspend fun getHabitsByDate(date: LocalDate): Flow<List<Habit>>

    suspend fun getHabitById(id: Int): Habit?

    suspend fun getHabitIdByRowId(rowId: Long): Int?

    suspend fun getHabitDateProgress(habitId: Int, progressDate: LocalDate): HabitProgression?

    suspend fun getHabitProgressById(id: Int): HabitProgression?

    suspend fun deleteHabitProgress(habitProgress: HabitProgression)

    suspend fun getAllHabits(): Flow<List<Habit>>

}