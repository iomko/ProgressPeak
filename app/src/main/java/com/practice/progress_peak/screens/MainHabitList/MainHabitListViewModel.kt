package com.practice.progress_peak.screens.MainHabitList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.progress_peak.data.ProgressPeakRepository
import com.practice.progress_peak.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainHabitListViewModel @Inject constructor(
    private val repository: ProgressPeakRepository): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
            is MainHabitListEvent.ChangeCurrentDate -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ReloadHabits(repository.getHabitsByDate(event.date)))
                }
            }
        }
    }
}