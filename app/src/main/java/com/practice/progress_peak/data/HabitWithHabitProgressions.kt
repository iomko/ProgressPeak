package com.practice.progress_peak.data

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithHabitProgressions(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val habitProgressions: List<HabitProgression>
)
