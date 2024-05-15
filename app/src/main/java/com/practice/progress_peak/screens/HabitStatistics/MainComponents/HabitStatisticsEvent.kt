package com.practice.progress_peak.screens.HabitStatistics.MainComponents

sealed class HabitStatisticsEvent {
    data class ExpandHabits(val expanded: Boolean): HabitStatisticsEvent()

    data class ChangeHabit(val index: Int): HabitStatisticsEvent()

    object EnterMainHabitListScreen: HabitStatisticsEvent()
}