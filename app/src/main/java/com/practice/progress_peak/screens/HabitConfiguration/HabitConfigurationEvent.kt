package com.practice.progress_peak.screens.HabitConfiguration

sealed class HabitConfigurationEvent {
    data class ChangeName(val name: String): HabitConfigurationEvent()
}