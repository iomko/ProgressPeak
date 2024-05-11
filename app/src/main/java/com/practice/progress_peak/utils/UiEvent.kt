package com.practice.progress_peak.utils

import com.practice.progress_peak.data.Habit
import kotlinx.coroutines.flow.Flow

sealed class UiEvent {
    object PopBack: UiEvent()
    data class Navigate(val route: String): UiEvent()
}