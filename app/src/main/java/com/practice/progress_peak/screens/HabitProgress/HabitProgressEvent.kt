package com.practice.progress_peak.screens.HabitProgress

sealed class HabitProgressEvent {

    object ChangeOperation: HabitProgressEvent()

    object ApplyInput: HabitProgressEvent()

    data class ChangeInput(val input: Int): HabitProgressEvent()

    object Done: HabitProgressEvent()
}