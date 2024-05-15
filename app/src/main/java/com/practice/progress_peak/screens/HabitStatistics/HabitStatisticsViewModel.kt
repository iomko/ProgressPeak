package com.practice.progress_peak.screens.HabitStatistics

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkeppeler.sheets.list.models.ListOption
import com.practice.progress_peak.data.Habit
import com.practice.progress_peak.data.HabitProgression
import com.practice.progress_peak.data.ProgressPeakRepository
import com.practice.progress_peak.screens.HabitProgress.HabitProgressEvent
import com.practice.progress_peak.screens.HabitProgress.Operation
import com.practice.progress_peak.screens.MainHabitList.MainHabitListEvent
import com.practice.progress_peak.screens.MainHabitList.OrderingType
import com.practice.progress_peak.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

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
                        StatisticsBox(Icons.Default.Favorite, averageDayProgress(firstHabitInList), "Daily Average Progress"),
                        StatisticsBox(Icons.Default.Share, maximumDayProgress(firstHabitInList), "Maximum Progress in Day"),
                        StatisticsBox(Icons.Default.Settings, activeDaysCount(firstHabitInList), "Active Days Count"),
                        StatisticsBox(Icons.Default.Settings, totalProgressCount(firstHabitInList), "Total Progress")
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
                        StatisticsBox(Icons.Default.Favorite, averageDayProgress(currentSelectedHabit), "Daily Average Progress"),
                        StatisticsBox(Icons.Default.Share, maximumDayProgress(currentSelectedHabit), "Maximum Progress in Day"),
                        StatisticsBox(Icons.Default.Settings, activeDaysCount(currentSelectedHabit), "Active Days Count"),
                        StatisticsBox(Icons.Default.Settings, totalProgressCount(currentSelectedHabit), "Total Progress")
                    )
                    this@HabitStatisticsViewModel.name = currentSelectedHabit.name
                }
            }
            is HabitStatisticsEvent.EnterMainHabitListScreen -> {
                viewModelScope.launch {
                    val navigationString = "main_habit_list_screen"
                    _uiEvent.send(UiEvent.Navigate(navigationString))
                }
            }
            else-> Unit
        }
    }

    private suspend fun averageDayProgress(habit: Habit): Double {
        val lastHabitProgress = repository.getHabitDateProgress(habit.id!!, habit.endDate!!)
        val numberOfDays = ChronoUnit.DAYS.between(habit.startDate, habit.endDate).toInt() + 1
        val averagePerDay: Double = lastHabitProgress?.progressToDate!! / numberOfDays.toDouble()

        return averagePerDay
    }

    private suspend fun maximumDayProgress(habit: Habit): Int {

        val endDate = habit.endDate
        var currentDate = habit.startDate

        var maximumProgressCount: Int = 0;
        while (currentDate!! <= endDate) {
            val nextHabitProgress: HabitProgression? = repository.getHabitDateProgress(habit.id!!, currentDate)

            if(nextHabitProgress?.progressThisDate!! > maximumProgressCount){
                maximumProgressCount = nextHabitProgress.progressThisDate
            }

            currentDate = currentDate.plusDays(1)
        }

        return maximumProgressCount
    }

    private suspend fun activeDaysCount(habit: Habit): Int {

        val endDate = habit.endDate
        var currentDate = habit.startDate

        var activeDaysCount: Int = 0;
        while (currentDate!! <= endDate) {
            val nextHabitProgress: HabitProgression? = repository.getHabitDateProgress(habit.id!!, currentDate)

            if(nextHabitProgress?.progressThisDate!! > 0){
                ++activeDaysCount
            }

            currentDate = currentDate.plusDays(1)
        }

        return activeDaysCount
    }

    private suspend fun totalProgressCount(habit: Habit): Int {
        var lastProgress = repository.getHabitDateProgress(habit.id!! ,habit.endDate!!)
        return lastProgress!!.progressToDate.toInt()
    }
}

class StatisticsBox(
    var icon: ImageVector,
    var count: Number,
    var text: String
)