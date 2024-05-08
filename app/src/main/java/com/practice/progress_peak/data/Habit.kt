package com.practice.progress_peak.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Habit(
    val name: String,
    val icon: Int,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val difficulty: String,
    val completed: Boolean,
    @PrimaryKey val id: Int? = null
)