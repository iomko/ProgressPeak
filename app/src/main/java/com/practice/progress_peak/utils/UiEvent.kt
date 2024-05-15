package com.practice.progress_peak.utils

sealed class UiEvent {
    object PopBack: UiEvent()

    data class ShowSnackbar(val message: String, val action: String? = null): UiEvent()
    data class Navigate(val route: String): UiEvent()
}