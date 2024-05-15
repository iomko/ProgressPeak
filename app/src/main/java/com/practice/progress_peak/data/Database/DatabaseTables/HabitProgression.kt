package com.practice.progress_peak.data.Database.DatabaseTables

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

//Spôsob vytvárania tabuľky bol navrhnutý spôsobom ako na video tutoriále: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
@Entity
data class HabitProgression(
    val date: LocalDate? = LocalDate.now(),
    val progressToDate: Int,
    val progressThisDate: Int,
    val habitId: Int? = null,
    @PrimaryKey
    val id: Int? = null
)