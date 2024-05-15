package com.practice.progress_peak.screens.HabitStatistics.MainComponents

//Nápad pre vytvorenie triedy Event som čerpal z tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
sealed class HabitStatisticsEvent {
    data class ExpandHabits(val expanded: Boolean): HabitStatisticsEvent()

    data class ChangeHabit(val index: Int): HabitStatisticsEvent()

    object EnterMainHabitListScreen: HabitStatisticsEvent()
}