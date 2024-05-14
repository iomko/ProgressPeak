package com.practice.progress_peak.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class HabitProgression(
    val date: LocalDate? = LocalDate.now(),
    val progressToDate: Int,
    val progressThisDate: Int,
    val habitId: Int? = null,
    @PrimaryKey
    val id: Int? = null
)