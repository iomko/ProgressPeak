package com.practice.progress_peak.screens.HabitProgress.MainComponents

sealed class HabitProgressEvent {

    object ChangeOperation: HabitProgressEvent()

    object ApplyInput: HabitProgressEvent()

    data class ChangeInput(val input: Int): HabitProgressEvent()

    object Done: HabitProgressEvent()
}