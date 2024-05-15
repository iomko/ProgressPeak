package com.practice.progress_peak.screens.HabitProgress.MainComponents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression
import com.practice.progress_peak.data.Database.Dao.ProgressPeakRepository
import com.practice.progress_peak.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

//Spôsob vytvárania ViewModelov som robil na základe
//tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
@HiltViewModel
class HabitProgressViewModel @Inject constructor (
    private val repository: ProgressPeakRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var habitProgress by mutableStateOf<HabitProgression?>(null)
        private set

    var habit by mutableStateOf<Habit?>(null)
        private set

    var name by mutableStateOf("")
        private set

    var habitProgressionAmount by mutableIntStateOf(0)
        private set

    var habitProgressionGoal by mutableIntStateOf(0)
        private set

    var currentOperation by mutableStateOf(Operation.Adding)
        private set

    var currentDate by mutableStateOf(LocalDate.now())
        private set

    var currentInputAmount by mutableIntStateOf(0)
        private set


    init{
        val habitId: Int? = savedStateHandle.get<Int>("habitId")

        val habitProgressId: Int? = savedStateHandle.get<Int>("habitProgressId")

        if (habitProgressId != null && habitProgressId != -1 && habitId != null && habitId != -1) {
            viewModelScope.launch {
                repository.getHabitProgressById(habitProgressId)?.let { habitProgress ->
                    habitProgressionAmount = habitProgress.progressToDate
                    currentDate = habitProgress.date
                    this@HabitProgressViewModel.habitProgress = habitProgress
                }

                repository.getHabitById(habitId)?.let{habit ->
                    name = habit.name
                    habitProgressionGoal = habit.goalAmount
                    this@HabitProgressViewModel.habit = habit
                }
            }
        }
    }

    fun onEvent(event: HabitProgressEvent) {
        when (event) {
            is HabitProgressEvent.ChangeOperation -> {
                currentOperation = if (currentOperation == Operation.Adding) Operation.Subtracting else Operation.Adding
            }
            is HabitProgressEvent.Done -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.PopBack)
                }
            }
            is HabitProgressEvent.ApplyInput -> {
                viewModelScope.launch {


                    if (currentOperation == Operation.Subtracting) {

                        if (currentInputAmount <= habitProgressionAmount) {
                            var previousHabitProgress: HabitProgression? = null
                            previousHabitProgress = habit?.id?.let {
                                repository.getHabitDateProgress(it, currentDate.minusDays(1))
                            }
                            if (previousHabitProgress != null) {
                                if (previousHabitProgress.progressToDate <= (habitProgressionAmount - currentInputAmount)) {

                                    val endDate = this@HabitProgressViewModel.habit?.endDate
                                    var currentDate = this@HabitProgressViewModel.currentDate

                                    if (currentDate != null) {

                                        while (currentDate!! <= endDate) {

                                            val nextHabitProgress: HabitProgression? = repository.getHabitDateProgress(habit?.id!!, currentDate)


                                            if (currentDate == this@HabitProgressViewModel.currentDate) {
                                                habitProgressionAmount =
                                                    nextHabitProgress!!.progressToDate - currentInputAmount

                                                repository.insertHabitProgression(
                                                    HabitProgression(
                                                        date = nextHabitProgress.date,
                                                        progressToDate = nextHabitProgress.progressToDate - currentInputAmount,
                                                        habitId = this@HabitProgressViewModel.habit?.id,
                                                        id = nextHabitProgress.id,
                                                        progressThisDate = nextHabitProgress.progressThisDate - currentInputAmount
                                                    )
                                                )
                                            } else {
                                                repository.insertHabitProgression(
                                                    HabitProgression(
                                                        date = nextHabitProgress!!.date,
                                                        progressToDate = (nextHabitProgress.progressToDate - currentInputAmount),
                                                        habitId = this@HabitProgressViewModel.habit?.id,
                                                        id = nextHabitProgress.id,
                                                        progressThisDate = nextHabitProgress.progressThisDate
                                                    )
                                                )
                                            }

                                            currentDate = currentDate.plusDays(1)
                                        }
                                    }

                                }
                            } else {

                                val endDate = this@HabitProgressViewModel.habit?.endDate
                                var currentDate = this@HabitProgressViewModel.currentDate

                                if (currentDate != null) {

                                    while (currentDate!! <= endDate) {

                                        val nextHabitProgress: HabitProgression? = repository.getHabitDateProgress(habit?.id!!, currentDate)


                                        if (currentDate == this@HabitProgressViewModel.currentDate) {
                                            habitProgressionAmount =
                                                nextHabitProgress!!.progressToDate - currentInputAmount

                                            repository.insertHabitProgression(
                                                HabitProgression(
                                                    date = nextHabitProgress.date,
                                                    progressToDate = nextHabitProgress.progressToDate - currentInputAmount,
                                                    habitId = this@HabitProgressViewModel.habit?.id,
                                                    id = nextHabitProgress.id,
                                                    progressThisDate = nextHabitProgress.progressThisDate - currentInputAmount
                                                )
                                            )
                                        } else {
                                            repository.insertHabitProgression(
                                                HabitProgression(
                                                    date = nextHabitProgress!!.date,
                                                    progressToDate = (nextHabitProgress.progressToDate - currentInputAmount),
                                                    habitId = this@HabitProgressViewModel.habit?.id,
                                                    id = nextHabitProgress.id,
                                                    progressThisDate = nextHabitProgress.progressThisDate
                                                )
                                            )
                                        }

                                        currentDate = currentDate.plusDays(1)
                                    }
                                }


                            }
                        }


                    } else {

                        val endDate = this@HabitProgressViewModel.habit?.endDate
                        var currentDate = this@HabitProgressViewModel.currentDate

                        if (currentDate != null) {

                            while (currentDate!! <= endDate) {
                                val nextHabitProgress: HabitProgression? = repository.getHabitDateProgress(habit?.id!!, currentDate)


                                if (currentDate == this@HabitProgressViewModel.currentDate) {
                                    habitProgressionAmount =
                                        nextHabitProgress!!.progressToDate + currentInputAmount

                                    repository.insertHabitProgression(
                                        HabitProgression(
                                            date = nextHabitProgress.date,
                                            progressToDate = (nextHabitProgress.progressToDate + currentInputAmount),
                                            habitId = this@HabitProgressViewModel.habit?.id,
                                            id = nextHabitProgress.id,
                                            progressThisDate = nextHabitProgress.progressThisDate + currentInputAmount
                                        )
                                    )

                                } else {

                                    repository.insertHabitProgression(
                                        HabitProgression(
                                            date = nextHabitProgress!!.date,
                                            progressToDate = (nextHabitProgress.progressToDate + currentInputAmount),
                                            habitId = this@HabitProgressViewModel.habit?.id,
                                            id = nextHabitProgress.id,
                                            progressThisDate = nextHabitProgress.progressThisDate
                                        )
                                    )

                                }


                                currentDate = currentDate.plusDays(1)
                            }
                        }


                    }

                    repository.insertHabit(
                        Habit(
                            name = habit?.name!!,
                            icon = habit?.icon!!,
                            habitType = habit?.habitType!!,
                            startDate = habit?.startDate!!,
                            endDate = habit?.endDate!!,
                            unitType = habit?.unitType!!,
                            goalAmount = habit?.goalAmount!!,
                            id = habit?.id
                        )
                    )

                }
            }
            is HabitProgressEvent.ChangeInput -> {
                currentInputAmount = event.input
            }
            else -> {}
        }
    }

}

enum class Operation {
    Adding,
    Subtracting
}