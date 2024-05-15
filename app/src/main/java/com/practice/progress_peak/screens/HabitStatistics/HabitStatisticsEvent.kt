package com.practice.progress_peak.screens.HabitStatistics

import com.practice.progress_peak.screens.HabitProgress.HabitProgressEvent
import com.practice.progress_peak.screens.MainHabitList.MainHabitListEvent

sealed class HabitStatisticsEvent {
    data class ExpandHabits(val expanded: Boolean): HabitStatisticsEvent()

    data class ChangeHabit(val index: Int): HabitStatisticsEvent()

    object EnterMainHabitListScreen: HabitStatisticsEvent()
}