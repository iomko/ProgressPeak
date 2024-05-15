package com.practice.progress_peak.screens.MainHabitList.MainComponents

import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression
import java.time.LocalDate

sealed class MainHabitListEvent {
    data class DeleteHabit(val habit: Habit) : MainHabitListEvent()
    data class EditHabit(val habit: Habit) : MainHabitListEvent()
    data class EditHabitProgress(val habit: Habit, val habitProgress: HabitProgression): MainHabitListEvent()
    object AddHabit: MainHabitListEvent()
    object GoToNextWeek: MainHabitListEvent()
    object GoToPreviousWeek: MainHabitListEvent()
    data class PickDate (val date: LocalDate): MainHabitListEvent()
    data class ChangeHabitType(val habitType: HabitType): MainHabitListEvent()

    data class ChangeSortByType(val index: Int): MainHabitListEvent()

    data class ExpandSortType(val expanded: Boolean): MainHabitListEvent()

    data class ExpandOrderingType(val expanded: Boolean): MainHabitListEvent()
    data class ChangeOrderingType(val index: Int): MainHabitListEvent()

    object EnterStatisticsScreen: MainHabitListEvent()
}