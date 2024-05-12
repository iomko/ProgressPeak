package com.practice.progress_peak.screens.HabitConfiguration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.progress_peak.data.Habit
import com.practice.progress_peak.data.ProgressPeakRepository
import com.practice.progress_peak.screens.MainHabitList.HabitType
import com.practice.progress_peak.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class HabitConfigurationViewModel @Inject constructor (
    private val repository: ProgressPeakRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var habit by mutableStateOf<Habit?>(null)
        private set

    var name by mutableStateOf("")
        private set

    var selectedIcon by mutableStateOf("😊")
        private set

    var expandedIcon by mutableStateOf(false)
        private set

    var habitType by mutableStateOf(HabitType.Good.value)
        private set

    var expandStartDate by mutableStateOf(false)
        private set

    var expandEndDate by mutableStateOf(false)
        private set

    var selectedStartDate by mutableStateOf<LocalDate?>(LocalDate.now())
        private set

    var selectedEndDate by mutableStateOf<LocalDate?>(LocalDate.now())
        private set

    var selectedGoalAmount by mutableIntStateOf(0)
        private set

    var selectedUnitType by mutableStateOf("km")
        private set

    var expandUnitType by mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init{

        val habitId: Int? = savedStateHandle.get<Int>("habitId")

        if (habitId != null && habitId != -1) {
            viewModelScope.launch {
                repository.getHabitById(habitId)?.let { habit ->
                    name = habit.name
                    selectedIcon = habit.icon
                    selectedStartDate = habit.startDate
                    selectedEndDate = habit.endDate
                    habitType = habit.habitType
                    selectedGoalAmount = habit.goalAmount
                    //more will be added later
                    this@HabitConfigurationViewModel.habit = habit
                }
            }
        }


    }

    fun onEvent(event: HabitConfigurationEvent){
        when(event){
            is HabitConfigurationEvent.ChangeName -> {
                name = event.name
            }
            is HabitConfigurationEvent.ChangeIcon -> {
                selectedIcon = event.icon
            }
            is HabitConfigurationEvent.ExpandIcon -> {
                expandedIcon = event.expanded
            }
            is HabitConfigurationEvent.ChangeHabitType -> {
                habitType = event.habitType.value
            }
            is HabitConfigurationEvent.ChangeStartDate -> {
                selectedStartDate = event.startDate
            }
            is HabitConfigurationEvent.ExpandStartDate -> {
                expandStartDate = event.expanded
            }
            is HabitConfigurationEvent.ChangeEndDate -> {
                selectedEndDate = event.endDate
            }
            is HabitConfigurationEvent.ExpandEndDate -> {
                expandEndDate = event.expanded
            }
            is HabitConfigurationEvent.AddHabit -> {
                viewModelScope.launch {
                    repository.insertHabit(
                        Habit(name = this@HabitConfigurationViewModel.name,
                            icon = this@HabitConfigurationViewModel.selectedIcon,
                            startDate = this@HabitConfigurationViewModel.selectedStartDate,
                            endDate = this@HabitConfigurationViewModel.selectedEndDate,
                            difficulty = "test",
                            completed = true,
                            habitType = this@HabitConfigurationViewModel.habitType,
                            goalAmount = this@HabitConfigurationViewModel.selectedGoalAmount,
                            unitType = this@HabitConfigurationViewModel.selectedUnitType,
                            id = habit?.id))
                    _uiEvent.send(UiEvent.PopBack)
                }
            }
            is HabitConfigurationEvent.CancelHabit -> {

            }
            is HabitConfigurationEvent.ExpandUnitType -> {
                expandUnitType = event.expanded
            }
            is HabitConfigurationEvent.ChangeGoalAmount -> {
                selectedGoalAmount = event.amount
            }
            is HabitConfigurationEvent.ChangeUnitType -> {
                selectedUnitType = event.type
            }

            else -> {}
        }
    }
}