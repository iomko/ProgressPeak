package com.practice.progress_peak.utils

//Spôsob vytvárania eventov, ktoré sú posielané z ViewModelu do UI bol navrhnutý za pomoci video tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
sealed class UiEvent {
    object PopBack: UiEvent()

    data class ShowSnackbar(val message: String, val action: String? = null): UiEvent()
    data class Navigate(val route: String): UiEvent()
}