package com.practice.progress_peak.data.Database.Dao


import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

//Spôsob vytvoránia repozitára bolo navrhnuté podobným spôsobom
//pomocou video tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ
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