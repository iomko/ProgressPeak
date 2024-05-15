package com.practice.progress_peak.data.Database.DatabaseAssociations

import androidx.room.Embedded
import androidx.room.Relation
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression

data class HabitWithHabitProgressions(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val habitProgressions: List<HabitProgression>
)
