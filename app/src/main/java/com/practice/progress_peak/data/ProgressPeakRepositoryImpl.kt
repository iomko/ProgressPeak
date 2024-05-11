package com.practice.progress_peak.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ProgressPeakRepositoryImpl(
    private val dao: ProgressPeakDao
) : ProgressPeakRepository {

    override suspend fun insertHabit(habit: Habit) {
        dao.insertHabit(habit)
    }

    override suspend fun deleteHabit(habit: Habit) {
        dao.deleteHabit(habit)
    }

    override fun getHabitsByDate(date: LocalDate): Flow<List<Habit>> {
        return dao.getHabitsByDate(date)
    }

    override suspend fun getHabitById(id: Int): Habit? {
        return dao.getHabitById(id)
    }


}