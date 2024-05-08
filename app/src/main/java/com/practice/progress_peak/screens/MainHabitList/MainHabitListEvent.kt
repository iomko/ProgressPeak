package com.practice.progress_peak.screens.MainHabitList

import com.practice.progress_peak.data.Habit
import java.time.LocalDate

sealed class MainHabitListEvent {
    data class DeleteHabit(val habit: Habit) : MainHabitListEvent()
    data class EditHabit(val habit: Habit) : MainHabitListEvent()
    object AddHabit: MainHabitListEvent()
    data class ChangeCurrentDate(val date: LocalDate): MainHabitListEvent()
}