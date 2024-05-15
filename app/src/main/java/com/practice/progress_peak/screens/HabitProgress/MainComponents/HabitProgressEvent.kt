package com.practice.progress_peak.screens.HabitProgress.MainComponents

//Nápad pre vytvorenie triedy Event som čerpal z tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
sealed class HabitProgressEvent {

    object ChangeOperation: HabitProgressEvent()

    object ApplyInput: HabitProgressEvent()

    data class ChangeInput(val input: Int): HabitProgressEvent()

    object Done: HabitProgressEvent()
}