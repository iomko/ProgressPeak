package com.practice.progress_peak.screens.MainHabitList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.progress_peak.data.ProgressPeakRepository
import com.practice.progress_peak.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainHabitListViewModel @Inject constructor(
    private val repository: ProgressPeakRepository): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var habits = repository.getHabitsByDate(LocalDate.now())

    var currentDate by mutableStateOf(LocalDate.now())
        private set

    var selectedDate by mutableStateOf(LocalDate.now())
        private set

    var currentHabitType by mutableStateOf(HabitType.Good)
        private set

    var currentSortType by mutableStateOf("Option 1")
        private set

    var currentCategoriesType by mutableStateOf("Option A")
        private set

    var currentTagsType by mutableStateOf("Choice X")
        private set

    var expandedSortType by mutableStateOf(false)
        private set

    var expandedCategoriesType by mutableStateOf(false)
        private set

    var expandedTagsType by mutableStateOf(false)
        private set


    val sortListOptions = listOf("Option 1", "Option 2", "Option 3")
    val categoriesListOptions = listOf("Option A", "Option B", "Option C")
    val tagsListOptions = listOf("Choice X", "Choice Y", "Choice Z")

    fun onEvent(event: MainHabitListEvent) {
        when(event){
            is MainHabitListEvent.AddHabit -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate("habit_configuration_screen"))
                }
            }
            is MainHabitListEvent.DeleteHabit -> {
                viewModelScope.launch {
                    repository.deleteHabit(event.habit)
                }
            }
            is MainHabitListEvent.EditHabit -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate("habit_configuration_screen" + "/${event.habit.id}"))
                }
            }
            is MainHabitListEvent.GoToNextWeek -> {
                viewModelScope.launch {
                    habits = repository.getHabitsByDate(currentDate)
                    currentDate = currentDate.plusWeeks(1)
                }
            }
            is MainHabitListEvent.GoToPreviousWeek -> {
                viewModelScope.launch {
                    habits = repository.getHabitsByDate(currentDate)
                    currentDate = currentDate.minusWeeks(1)
                }
            }
            is MainHabitListEvent.PickDate -> {
                viewModelScope.launch {
                    habits = repository.getHabitsByDate(event.date)
                    selectedDate = event.date
                }
            }
            is MainHabitListEvent.ChangeHabitType -> {
                viewModelScope.launch {
                    currentHabitType = event.habitType
                }
            }
            is MainHabitListEvent.ChangeSortByType -> {
                viewModelScope.launch {
                    currentSortType = event.sortByType
                }
            }
            is MainHabitListEvent.ChangeCategoriesType -> {
                viewModelScope.launch {
                    currentCategoriesType = event.categoriesType
                }
            }
            is MainHabitListEvent.ChangeTagsType -> {
                viewModelScope.launch {
                    currentTagsType = event.tagsType
                }
            }
            is MainHabitListEvent.ExpandSortType -> {
                viewModelScope.launch {
                    expandedSortType = event.expanded
                }
            }
            is MainHabitListEvent.ExpandCategoriesType -> {
                viewModelScope.launch {
                    expandedCategoriesType = event.expanded
                }
            }
            is MainHabitListEvent.ExpandTagsType -> {
                viewModelScope.launch {
                    expandedTagsType = event.expanded
                }
            }

            else -> Unit
        }
    }
}

// Add HabitType enum in the same file
enum class HabitType {
    Good,
    Bad
}