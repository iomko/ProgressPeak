package com.practice.progress_peak.screens.HabitStatistics.OtherComponents

import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression
import com.practice.progress_peak.data.Database.Dao.ProgressPeakRepository
import java.time.temporal.ChronoUnit


suspend fun averageDayProgress(habit: Habit, habitsRepository: ProgressPeakRepository): Double {
    val lastHabitProgress = habitsRepository.getHabitDateProgress(habit.id!!, habit.endDate!!)
    val numberOfDays = ChronoUnit.DAYS.between(habit.startDate, habit.endDate).toInt() + 1
    val averagePerDay: Double = lastHabitProgress?.progressToDate!! / numberOfDays.toDouble()

    return averagePerDay
}

suspend fun maximumDayProgress(habit: Habit, habitsRepository: ProgressPeakRepository): Int {

    val endDate = habit.endDate
    var currentDate = habit.startDate

    var maximumProgressCount: Int = 0;
    while (currentDate!! <= endDate) {
        val nextHabitProgress: HabitProgression? = habitsRepository.getHabitDateProgress(habit.id!!, currentDate)

        if(nextHabitProgress?.progressThisDate!! > maximumProgressCount){
            maximumProgressCount = nextHabitProgress.progressThisDate
        }

        currentDate = currentDate.plusDays(1)
    }

    return maximumProgressCount
}

suspend fun activeDaysCount(habit: Habit, habitsRepository: ProgressPeakRepository): Int {

    val endDate = habit.endDate
    var currentDate = habit.startDate

    var activeDaysCount: Int = 0;
    while (currentDate!! <= endDate) {
        val nextHabitProgress: HabitProgression? = habitsRepository.getHabitDateProgress(habit.id!!, currentDate)

        if(nextHabitProgress?.progressThisDate!! > 0){
            ++activeDaysCount
        }

        currentDate = currentDate.plusDays(1)
    }

    return activeDaysCount
}

suspend fun totalProgressCount(habit: Habit, habitsRepository: ProgressPeakRepository): Int {
    var lastProgress = habitsRepository.getHabitDateProgress(habit.id!! ,habit.endDate!!)
    return lastProgress!!.progressToDate.toInt()
}