package com.practice.progress_peak.data


import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ProgressPeakRepository {

    suspend fun insertHabit(habit: Habit)

    suspend fun deleteHabit(habit: Habit)

    fun getHabitsByDate(date: LocalDate): Flow<List<Habit>>

    suspend fun getHabitById(id: Int): Habit?

}