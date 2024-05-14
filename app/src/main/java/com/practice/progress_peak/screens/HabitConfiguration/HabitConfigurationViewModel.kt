package com.practice.progress_peak.screens.HabitConfiguration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import com.practice.progress_peak.data.Habit
import com.practice.progress_peak.data.HabitProgression
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

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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

                    if(habit == null){
                        //tak netreba nic menit

                        val rowId = repository.insertHabit(Habit(
                            name = this@HabitConfigurationViewModel.name,
                            icon = this@HabitConfigurationViewModel.selectedIcon,
                            startDate = this@HabitConfigurationViewModel.selectedStartDate,
                            endDate = this@HabitConfigurationViewModel.selectedEndDate,
                            difficulty = "test",
                            completed = true,
                            habitType = this@HabitConfigurationViewModel.habitType,
                            goalAmount = this@HabitConfigurationViewModel.selectedGoalAmount,
                            unitType = this@HabitConfigurationViewModel.selectedUnitType,
                            id = habit?.id)
                        )

                        val habitId = repository.getHabitIdByRowId(rowId)


                        val startDate: LocalDate = this@HabitConfigurationViewModel.selectedStartDate ?: return@launch
                        val endDate: LocalDate = this@HabitConfigurationViewModel.selectedEndDate ?: return@launch
                        var currentDate: LocalDate = startDate

                        while (currentDate <= endDate) {
                            repository.insertHabitProgression(
                                HabitProgression(
                                    date = currentDate,
                                    habitId = habitId,
                                    progressToDate = 0,
                                    progressThisDate = 0
                                )
                            )
                            currentDate = currentDate.plusDays(1)
                        }

                    } else {

                        var dataInFutureNeedChanges = false
                        var differenceAmount:Int = 0
                        //musime updatnut progress
                        //zoberem si vsetky habitProgressions
                        if(habit?.startDate!! > selectedStartDate){

                            //tak musime pridat na dane miesta nove entries
                            val startDate: LocalDate = selectedStartDate!!
                            val endDate: LocalDate = habit?.startDate!!
                            var currentDate: LocalDate = startDate

                            while (currentDate < endDate) {
                                repository.insertHabitProgression(
                                    HabitProgression(
                                        date = currentDate,
                                        habitId = habit?.id,
                                        progressToDate = 0,
                                        progressThisDate = 0
                                    )
                                )
                                currentDate = currentDate.plusDays(1)
                            }

                        } else if (habit?.startDate!! < selectedStartDate){
                            //musime si zapamatat o kolko mame znizit progress pre vsetky dni ktore su za tymto dnom

                            val startDate: LocalDate = habit?.startDate!!
                            val endDate: LocalDate = selectedStartDate!!
                            var currentDate: LocalDate = startDate

                            //val startingProgress = repository.getHabitDateProgress(habit?.id!!, startDate)
                            val endingProgress = repository.getHabitDateProgress(habit?.id!!, endDate)

                            if(endingProgress?.progressToDate != 0){
                                dataInFutureNeedChanges = true

                                val newStartDate: LocalDate = selectedStartDate?.minusDays(1)!!
                                val newStartDateProgress = repository.getHabitDateProgress(habit?.id!!, newStartDate)

                                differenceAmount = newStartDateProgress?.progressToDate!!
                            }


                            while (currentDate < endDate) {
                                val collectedHabitProgress = repository.getHabitDateProgress(habit?.id!!, currentDate)

                                repository.deleteHabitProgress(habitProgress = collectedHabitProgress!!)
                                currentDate = currentDate.plusDays(1)
                            }

                        }

                        if (habit?.endDate!! > selectedEndDate){

                            val startDate: LocalDate = habit?.endDate!!
                            val endDate: LocalDate = selectedEndDate!!
                            var currentDate: LocalDate = startDate


                            while (currentDate > endDate) {
                                val collectedHabitProgress = repository.getHabitDateProgress(habit?.id!!, currentDate)

                                repository.deleteHabitProgress(habitProgress = collectedHabitProgress!!)
                                currentDate = currentDate.minusDays(1)
                            }
                        } else if ( habit?.endDate!! < selectedEndDate && !dataInFutureNeedChanges){


                            val startDate: LocalDate = habit?.endDate?.plusDays(1)!!
                            val endDate: LocalDate = selectedEndDate!!
                            var currentDate: LocalDate = startDate

                            val oldEndHabitProgress = repository.getHabitDateProgress(habit?.id!!, habit?.endDate!!)

                            while (currentDate <= endDate) {

                                repository.insertHabitProgression(
                                    HabitProgression(
                                        date = currentDate,
                                        habitId = habit?.id,
                                        progressToDate = oldEndHabitProgress?.progressToDate!!,
                                        progressThisDate = 0,
                                    )
                                )

                                currentDate = currentDate.plusDays(1)
                            }


                        }

                        if(dataInFutureNeedChanges){

                            //val newStartDate: LocalDate = selectedStartDate!!
                            //val newStartDateProgress = repository.getHabitDateProgress(habit?.id!!, newStartDate)

                            //val differenceAmount = newStartDateProgress?.progressToDate!!


                            //pozrem sa ci musim vymazat nejake data z endu

                            //ideme dole

                            //musime len vymazavat

                            val startDate: LocalDate = selectedStartDate!!
                            val endDate: LocalDate = selectedEndDate!!
                            var currentDate: LocalDate = startDate


                            var hasExceededOriginalEnd = false
                            var OldEndcollectedHabitProgress = repository.getHabitDateProgress(habit?.id!!, habit?.endDate!!)
                            var collectedHabitProgress: HabitProgression? = null
                            while (currentDate <= endDate) {

                                if(!hasExceededOriginalEnd){
                                    collectedHabitProgress = repository.getHabitDateProgress(habit?.id!!, currentDate)
                                    if(collectedHabitProgress != null){

                                        repository.insertHabitProgression(
                                            HabitProgression(
                                                date = currentDate,
                                                habitId = habit?.id,
                                                progressToDate = collectedHabitProgress.progressToDate - differenceAmount,
                                                progressThisDate = collectedHabitProgress.progressThisDate,
                                                id = collectedHabitProgress.id
                                            )
                                        )

                                    } else {
                                        hasExceededOriginalEnd = true
                                        collectedHabitProgress = OldEndcollectedHabitProgress
                                    }

                                } else {

                                    repository.insertHabitProgression(
                                        HabitProgression(
                                            date = currentDate,
                                            habitId = habit?.id,
                                            progressToDate = collectedHabitProgress?.progressToDate!! - differenceAmount,
                                            progressThisDate = 0
                                        )
                                    )

                                }



                                currentDate = currentDate.plusDays(1)
                            }


                        }




                        //musime sa pozriet ci ideme s endDate hore alebo dole

                    }

                    repository.insertHabit(
                        Habit(
                            name = this@HabitConfigurationViewModel.name,
                            icon = this@HabitConfigurationViewModel.selectedIcon,
                            habitType = this@HabitConfigurationViewModel.habitType,
                            startDate = this@HabitConfigurationViewModel.selectedStartDate,
                            endDate = this@HabitConfigurationViewModel.selectedEndDate,
                            difficulty = "test",
                            completed = true,
                            unitType = this@HabitConfigurationViewModel.selectedUnitType,
                            goalAmount = this@HabitConfigurationViewModel.selectedGoalAmount,
                            id = habit?.id
                        )
                    )

                    _uiEvent.send(UiEvent.PopBack)
                }
            }
            is HabitConfigurationEvent.CancelHabit -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.PopBack)
                }
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