package com.practice.progress_peak.data.Database.DatabaseTables

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

//Spôsob vytvárania tabuľky bol navrhnutý spôsobom ako na video tutoriále: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
@Entity
data class Habit(
    val name: String,
    val icon: String,
    val habitType: Boolean,
    val startDate: LocalDate? = LocalDate.now(),
    val endDate: LocalDate? = LocalDate.now(),
    val unitType: String,
    val goalAmount: Int,
    @PrimaryKey val id: Int? = null
)