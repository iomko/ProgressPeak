package com.practice.progress_peak.screens.HabitConfiguration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.progress_peak.data.Habit
import com.practice.progress_peak.data.ProgressPeakRepository
import com.practice.progress_peak.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init{

        val habitId: Int? = savedStateHandle.get("habitId")

        if (habitId != null && habitId != -1) {
            viewModelScope.launch {
                repository.getHabitById(habitId)?.let { habit ->
                    name = habit.name
                    this@HabitConfigurationViewModel.habit = habit
                }
            }
        }


        /*
        val habitId = savedStateHandle.get<Int>("habitId")!!
        if(habitId != -1){
            viewModelScope.launch {
                repository.getHabitById(habitId)?.let{ habit ->
                    name = habit.name
                    this@HabitConfigurationViewModel.habit = habit
                }
            }
        }
         */
    }

    fun onEvent(event: HabitConfigurationEvent){
        when(event){
            is HabitConfigurationEvent.ChangeName -> {
                name = event.name
            }
        }
    }
}