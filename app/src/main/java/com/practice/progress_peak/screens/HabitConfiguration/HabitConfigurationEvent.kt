package com.practice.progress_peak.screens.HabitConfiguration

import com.practice.progress_peak.screens.MainHabitList.HabitType
import java.time.LocalDate

sealed class HabitConfigurationEvent {
    data class ChangeName(val name: String): HabitConfigurationEvent()

    data class ChangeIcon(val icon: String): HabitConfigurationEvent()
    data class ExpandIcon(val expanded: Boolean): HabitConfigurationEvent()

    data class ChangeHabitType(val habitType: HabitType): HabitConfigurationEvent()

    data class ChangeStartDate(val startDate: LocalDate?): HabitConfigurationEvent()

    data class ExpandStartDate(val expanded: Boolean): HabitConfigurationEvent()

    data class ChangeEndDate(val endDate: LocalDate?): HabitConfigurationEvent()

    data class ExpandEndDate(val expanded: Boolean): HabitConfigurationEvent()

    object AddHabit: HabitConfigurationEvent()

    object CancelHabit: HabitConfigurationEvent()

    object DeleteHabit: HabitConfigurationEvent()

    data class ChangeGoalAmount(val amount: Int): HabitConfigurationEvent()

    data class ChangeUnitType(val index: Int): HabitConfigurationEvent()

    data class ExpandUnitType(val expanded: Boolean): HabitConfigurationEvent()

}