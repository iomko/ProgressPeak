package com.practice.progress_peak.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ProgressPeakRepositoryImpl(
    private val dao: ProgressPeakDao
) : ProgressPeakRepository {

    override suspend fun insertHabit(habit: Habit): Long {
        return dao.insertHabit(habit)
    }

    override suspend fun insertHabitProgression(habitProgression: HabitProgression): Long {
        return dao.insertHabitProgression(habitProgression)
    }


    override suspend fun deleteHabit(habit: Habit) {
        dao.deleteHabit(habit)
    }

    override suspend fun getHabitsByDate(date: LocalDate): Flow<List<Habit>> {
        return dao.getHabitsByDate(date)
    }

    override suspend fun getHabitById(id: Int): Habit? {
        return dao.getHabitById(id)
    }

    override suspend fun getHabitIdByRowId(rowId: Long): Int? {
        return dao.getHabitIdByRowId(rowId)
    }

    override suspend fun getHabitDateProgress(
        habitId: Int,
        progressDate: LocalDate
    ): HabitProgression? {
        return dao.getHabitDateProgress(habitId, progressDate)
    }

    override suspend fun getHabitProgressById(id: Int): HabitProgression? {
        return dao.getHabitProgressById(id)
    }

    override suspend fun deleteHabitProgress(habitProgress: HabitProgression) {
        dao.deleteHabitProgress(habitProgress)
    }

    override suspend fun getAllHabits(): Flow<List<Habit>> {
        return dao.getAllHabits()
    }


}