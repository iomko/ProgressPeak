package com.practice.progress_peak.screens.MainHabitList

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkeppeler.sheets.list.models.ListOption
import com.practice.progress_peak.data.Habit
import com.practice.progress_peak.data.HabitProgression
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

    var currentSortTypeIndex by mutableIntStateOf(0)
        private set

    var currentOrderingTypeIndex by mutableIntStateOf(OrderingType.Ascending.value)
        private set

    var expandedOrderingType by mutableStateOf(false)
        private set

    var expandedSortType by mutableStateOf(false)
        private set

    var sortListOptions = mutableListOf<ListOption>(
        ListOption(titleText = "Name", selected = true),
        ListOption(titleText = "Completion")
    )

    val orderingListOptions = mutableListOf<ListOption>(
        ListOption(titleText = "Ascending", selected = true),
        ListOption(titleText = "Descending")
    )

    private val _habits = MutableStateFlow<List<Pair<Habit, HabitProgression>>>(emptyList())
    val habits: StateFlow<List<Pair<Habit, HabitProgression>>> = _habits.asStateFlow()

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
            is MainHabitListEvent.EditHabitProgress -> {
                viewModelScope.launch {
                    val navigationString = "habit_progress_screen" +
                            "/?habitId=${event.habit.id}/?habitProgressId=${event.habitProgress.id}"

                    _uiEvent.send(UiEvent.Navigate(navigationString))
                }
            }
            is MainHabitListEvent.EnterStatisticsScreen -> {
                viewModelScope.launch {
                    val navigationString = "habit_statistics_screen"
                    _uiEvent.send(UiEvent.Navigate(navigationString))
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
                    updateSelectedElementInListOfOptions(sortListOptions ,currentSortTypeIndex, false)
                    currentSortTypeIndex = event.index
                    updateSelectedElementInListOfOptions(sortListOptions, currentSortTypeIndex, true)
                    fetchData()
                }
            }
            is MainHabitListEvent.ExpandSortType -> {
                viewModelScope.launch {
                    expandedSortType = event.expanded
                }
            }
            is MainHabitListEvent.ExpandOrderingType -> {
                viewModelScope.launch {
                    expandedOrderingType = event.expanded
                }
            }
            is MainHabitListEvent.ChangeOrderingType -> {
                viewModelScope.launch {
                    updateSelectedElementInListOfOptions(orderingListOptions ,currentOrderingTypeIndex, false)
                    currentOrderingTypeIndex = event.index
                    updateSelectedElementInListOfOptions(orderingListOptions, currentOrderingTypeIndex, true)
                    fetchData()
                }
            }

            else -> Unit
        }
    }

    fun getStringNameFromListOfOptions(list: MutableList<ListOption>, index: Int): String {
        require(index in list.indices) { "Index out of bounds" }
        return list[index].titleText
    }

    private fun updateSelectedElementInListOfOptions(list: MutableList<ListOption>, index: Int, isSelected: Boolean) {
        require(index in list.indices) { "Index out of bounds" }
        val optionToUpdate = list[index]
        val updatedOption = optionToUpdate.copy(selected = isSelected)
        list[index] = updatedOption
    }


    private fun applyFiltering(listOfItems: List<Pair<Habit, HabitProgression>>): List<Pair<Habit, HabitProgression>> {
        return when (currentOrderingTypeIndex) {
            OrderingType.Ascending.value -> {
                listOfItems
                    .filter { (habit, habitProgress) ->
                        currentHabitType.value == habit.habitType
                    }
                    .sortedBy { (habit, habitProgress) ->
                        when (currentSortTypeIndex) {
                            SortType.Completion.value -> habitProgress.progressToDate.toString()
                            SortType.Name.value -> habit.name
                            else -> null
                        }
                    }
            }
            OrderingType.Descending.value -> {
                listOfItems
                    .filter { (habit, habitProgress) ->
                        currentHabitType.value == habit.habitType
                    }
                    .sortedByDescending { (habit, habitProgress) ->
                        when (currentSortTypeIndex) {
                            SortType.Completion.value -> habitProgress.progressToDate.toString()
                            SortType.Name.value -> habit.name
                            else -> null
                        }
                    }
            }
            else -> emptyList()
        }
    }

    private fun fetchData(){

        /*
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
         */
        Log.d("CURRENTDAY", selectedDate.toString())

        viewModelScope.launch {
            repository.getHabitsByDate(selectedDate)
                .map { listOfHabits ->
                    listOfHabits.mapNotNull { habit ->
                        val habitProgression = habit.id?.let { repository.getHabitDateProgress(it, selectedDate) }
                        habitProgression?.let { habit to it }
                    }
                }
                .collect { sortedList ->
                    val filteredList = applyFiltering(sortedList)
                    _habits.value = filteredList
                }
        }

        /*
        viewModelScope.launch {
            repository.getHabitsByDate(selectedDate)
                .map { listOfHabits ->
                    val filteredHabits = applyFiltering(listOfHabits)
                    listOfHabits
                        .map { habit ->
                            habit to habit.id?.let { repository.getHabitDateProgress(it, selectedDate) }
                        }
                        .map{
                            habit -> applyFiltering(habit)
                        }
                }
                .collect { sortedList ->
                    _habits.value = sortedList
                }
        }
         */

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
    Completion(1)
}