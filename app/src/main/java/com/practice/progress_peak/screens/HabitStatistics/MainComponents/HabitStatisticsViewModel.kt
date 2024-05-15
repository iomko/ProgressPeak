package com.practice.progress_peak.screens.HabitStatistics.MainComponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkeppeler.sheets.list.models.ListOption
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.Dao.ProgressPeakRepository
import com.practice.progress_peak.screens.HabitStatistics.OtherComponents.activeDaysCount
import com.practice.progress_peak.screens.HabitStatistics.OtherComponents.averageDayProgress
import com.practice.progress_peak.screens.HabitStatistics.OtherComponents.maximumDayProgress
import com.practice.progress_peak.screens.HabitStatistics.OtherComponents.totalProgressCount
import com.practice.progress_peak.utils.Routes
import com.practice.progress_peak.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitStatisticsViewModel @Inject constructor (
    private val repository: ProgressPeakRepository
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    var name by mutableStateOf("")
        private set

    var statisticsBoxes by mutableStateOf<List<StatisticsBox>>(emptyList())
        private set

    var expandedHabit by mutableStateOf(false)
        private set

    var habitNamesList = mutableListOf<ListOption>()
        private set

    var selectedHabitIndex by mutableIntStateOf(0)
        private set

    init {
        viewModelScope.launch {
            repository.getAllHabits().collect { habitsList ->
                _habits.value = habitsList

                if (habits.value.isNotEmpty()) {
                    val firstHabitInList = habits.value[0]
                    statisticsBoxes = listOf(
                        StatisticsBox(Icons.Default.Favorite, averageDayProgress(firstHabitInList, repository), "Daily Average Progress"),
                        StatisticsBox(Icons.Default.Share, maximumDayProgress(firstHabitInList, repository), "Maximum Progress in Day"),
                        StatisticsBox(Icons.Default.Settings, activeDaysCount(firstHabitInList, repository), "Active Days Count"),
                        StatisticsBox(Icons.Default.Settings, totalProgressCount(firstHabitInList, repository), "Total Progress")
                    )
                    this@HabitStatisticsViewModel.name = firstHabitInList.name

                    habits.value.forEach { habit ->
                        habitNamesList.add((ListOption(titleText = habit.name)))
                    }

                    updateSelectedOption(habitNamesList, 0, true)
                }
            }
        }
    }

    private fun updateSelectedOption(list: MutableList<ListOption>,index: Int, isSelected: Boolean) {
        require(index in list.indices) { "Index out of bounds" }
        val optionToUpdate = list[index]
        val updatedOption = optionToUpdate.copy(selected = isSelected)
        list[index] = updatedOption
    }


    fun onEvent(event: HabitStatisticsEvent) {
        when (event) {
            is HabitStatisticsEvent.ExpandHabits -> {
                expandedHabit = event.expanded
            }
            is HabitStatisticsEvent.ChangeHabit -> {
                viewModelScope.launch {
                    updateSelectedOption(habitNamesList , selectedHabitIndex, false)
                    selectedHabitIndex = event.index
                    updateSelectedOption(habitNamesList, selectedHabitIndex, true)

                    val currentSelectedHabit = habits.value[selectedHabitIndex]

                    statisticsBoxes = listOf(
                        StatisticsBox(Icons.Default.Favorite, averageDayProgress(currentSelectedHabit, repository), "Daily Average Progress"),
                        StatisticsBox(Icons.Default.Share, maximumDayProgress(currentSelectedHabit, repository), "Maximum Progress in Day"),
                        StatisticsBox(Icons.Default.Settings, activeDaysCount(currentSelectedHabit, repository), "Active Days Count"),
                        StatisticsBox(Icons.Default.Settings, totalProgressCount(currentSelectedHabit, repository), "Total Progress")
                    )
                    this@HabitStatisticsViewModel.name = currentSelectedHabit.name
                }
            }
            is HabitStatisticsEvent.EnterMainHabitListScreen -> {
                viewModelScope.launch {
                    val navigationString = Routes.MAIN_SCREEN
                    _uiEvent.send(UiEvent.Navigate(navigationString))
                }
            }
            else-> Unit
        }
    }
}

class StatisticsBox(
    var icon: ImageVector,
    var count: Number,
    var text: String
)