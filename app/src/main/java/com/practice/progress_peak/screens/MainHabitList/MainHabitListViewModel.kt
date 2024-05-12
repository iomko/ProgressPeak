package com.practice.progress_peak.screens.MainHabitList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkeppeler.sheets.list.models.ListOption
import com.practice.progress_peak.data.Habit
import com.practice.progress_peak.data.ProgressPeakRepository
import com.practice.progress_peak.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainHabitListViewModel @Inject constructor(
    private val repository: ProgressPeakRepository): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var currentDate by mutableStateOf(LocalDate.now())
        private set

    var selectedDate by mutableStateOf(LocalDate.now())
        private set

    var currentHabitType by mutableStateOf(HabitType.Good)
        private set

    var currentSortTypeIndex by mutableIntStateOf(SortType.Name.value)
        private set

    var currentOrderingTypeIndex by mutableIntStateOf(OrderingType.Ascending.value)
        private set

    var expandedOrderingType by mutableStateOf(false)
        private set

    var currentTagsType by mutableStateOf("Choice X")
        private set

    var expandedSortType by mutableStateOf(false)
        private set

    var expandedTagsType by mutableStateOf(false)
        private set

    var sortListOptions = mutableListOf<ListOption>(
        ListOption(titleText = "Name", selected = true),
        ListOption(titleText = "Completion")
    )

    val orderingListOptions = mutableListOf<ListOption>(
        ListOption(titleText = "Ascending", selected = true),
        ListOption(titleText = "Descending")
    )


    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    init {
        fetchData()
    }


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
                    _uiEvent.send(UiEvent.Navigate("habit_configuration_screen" + "?habitId=${event.habit.id}"))
                }
            }
            is MainHabitListEvent.GoToNextWeek -> {
                viewModelScope.launch {
                    //habits = repository.getHabitsByDate(currentDate)
                    currentDate = currentDate.plusWeeks(1)
                }
            }
            is MainHabitListEvent.GoToPreviousWeek -> {
                viewModelScope.launch {
                    //habits = repository.getHabitsByDate(currentDate)
                    currentDate = currentDate.minusWeeks(1)
                }
            }
            is MainHabitListEvent.PickDate -> {
                viewModelScope.launch {
                    selectedDate = event.date
                    fetchData()
                }
            }
            is MainHabitListEvent.ChangeHabitType -> {
                viewModelScope.launch {
                    currentHabitType = event.habitType
                    fetchData()
                }
            }
            is MainHabitListEvent.ChangeSortByType -> {
                viewModelScope.launch {
                    updateSelectedOption(sortListOptions ,currentSortTypeIndex, false)
                    currentSortTypeIndex = event.index
                    updateSelectedOption(sortListOptions, currentSortTypeIndex, true)
                    fetchData()
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
            is MainHabitListEvent.ExpandTagsType -> {
                viewModelScope.launch {
                    expandedTagsType = event.expanded
                }
            }
            is MainHabitListEvent.ExpandOrderingType -> {
                viewModelScope.launch {
                    expandedOrderingType = event.expanded
                }
            }
            is MainHabitListEvent.ChangeOrderingType -> {
                viewModelScope.launch {
                    updateSelectedOption(orderingListOptions ,currentOrderingTypeIndex, false)
                    currentOrderingTypeIndex = event.index
                    updateSelectedOption(orderingListOptions, currentOrderingTypeIndex, true)
                    fetchData()
                }
            }

            else -> Unit
        }
    }

    fun getStringNameFromIndex(list: MutableList<ListOption>, index: Int): String {
        require(index in list.indices) { "Index out of bounds" }
        return list[index].titleText
    }

    private fun updateSelectedOption(list: MutableList<ListOption>,index: Int, isSelected: Boolean) {
        require(index in list.indices) { "Index out of bounds" }
        val optionToUpdate = list[index]
        val updatedOption = optionToUpdate.copy(selected = isSelected)
        list[index] = updatedOption
    }


    private fun applyFiltering(listOfItems: List<Habit>): List<Habit>? {
        return when (currentOrderingTypeIndex) {
            OrderingType.Ascending.value -> {
                listOfItems
                    .filter { habit ->
                        currentHabitType.value == habit.habitType
                    }
                    .sortedWith(compareBy {
                        when (currentSortTypeIndex) {
                            SortType.Name.value -> it.name
                            else -> null
                        }
                    })
            }
            OrderingType.Descending.value -> {
                listOfItems
                    .filter { habit ->
                        currentHabitType.value == habit.habitType
                    }
                    .sortedWith(compareByDescending {
                        when (currentSortTypeIndex) {
                            SortType.Name.value -> it.name
                            else -> null
                        }
                    })
            }
            else -> null
        }
    }

    private fun fetchData(){
        viewModelScope.launch {
            repository.getHabitsByDate(selectedDate)
                .map { listOfItems ->
                    applyFiltering(listOfItems)
                }
                .collect { sortedList ->
                    if (sortedList != null) {
                        _habits.value = sortedList
                    }
                }
        }
    }

}


enum class HabitType(var value: Boolean) {
    Good(true),
    Bad(false)
}

enum class OrderingType(var value: Int) {
    Ascending(0),
    Descending(1)
}

enum class SortType(var value: Int){
    Name(0),
}