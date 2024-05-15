package com.practice.progress_peak.data.Database.DatabaseAssociations

import androidx.room.Embedded
import androidx.room.Relation
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression

//Spôsob vytvorenia vzťahov medzi tabuľkami bolo navrhnuté pomocou video tutoriálu: https://www.youtube.com/watch?v=K8yKH5Ak84E
data class HabitWithHabitProgressions(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val habitProgressions: List<HabitProgression>
)
