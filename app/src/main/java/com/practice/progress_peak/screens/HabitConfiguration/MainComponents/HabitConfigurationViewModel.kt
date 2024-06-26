package com.practice.progress_peak.screens.HabitConfiguration.MainComponents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxkeppeler.sheets.list.models.ListOption
import com.practice.progress_peak.R
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression
import com.practice.progress_peak.data.Database.Dao.ProgressPeakRepository
import com.practice.progress_peak.screens.MainHabitList.MainComponents.HabitType
import com.practice.progress_peak.utils.StringResourcesProvider
import com.practice.progress_peak.utils.UiEvent
import com.practice.progress_peak.utils.updateSelectedElementInListOfOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

//Spôsob vytvárania ViewModelov som robil na základe
//tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
@HiltViewModel
class HabitConfigurationViewModel @Inject constructor (
    private val stringResourcesProvider: StringResourcesProvider,
    private val repository: ProgressPeakRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var habit by mutableStateOf<Habit?>(null)
        private set

    var name by mutableStateOf(stringResourcesProvider.getString(R.string.habit_configuration_default_name))
        private set

    var selectedIcon by mutableStateOf(stringResourcesProvider.getString(R.string.habit_configuration_default_icon))
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

    var selectedUnitType by mutableStateOf(stringResourcesProvider.getString(R.string.habit_configuration_default_unit_type))
        private set

    var selectedUnitTypeIndex by mutableIntStateOf(0)
        private set


    var expandUnitType by mutableStateOf(false)
        private set


    var unitTypeOptions = mutableListOf<ListOption>(
        ListOption(titleText = stringResourcesProvider.getString(R.string.habit_configuration_first_unit_type), selected = true),
        ListOption(titleText = stringResourcesProvider.getString(R.string.habit_configuration_second_unit_type))
    )


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
                    selectedUnitType = habit.unitType
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
            is HabitConfigurationEvent.DeleteHabit -> {
                viewModelScope.launch {
                    if(habit == null){
                        _uiEvent.send(UiEvent.ShowSnackbar(
                            message = stringResourcesProvider.getString(R.string.scaffold_delete_error)
                        ))
                        return@launch
                    } else {
                        val startDate: LocalDate = habit?.endDate!!
                        val endDate: LocalDate = selectedEndDate!!
                        var currentDate: LocalDate = startDate


                        while (currentDate <= endDate) {
                            val collectedHabitProgress = repository.getHabitDateProgress(habit?.id!!, currentDate)
                            repository.deleteHabitProgress(habitProgress = collectedHabitProgress!!)
                            currentDate = currentDate.plusDays(1)
                        }

                        repository.deleteHabit(habit!!)

                        _uiEvent.send(UiEvent.PopBack)
                    }
                }

            }
            is HabitConfigurationEvent.AddHabit -> {

                viewModelScope.launch {

                    if (name.isEmpty()){
                    _uiEvent.send(UiEvent.ShowSnackbar(
                        message = stringResourcesProvider.getString(R.string.scaffold_name_error)
                    ))
                        return@launch
                    } else if (selectedStartDate!! > selectedEndDate){
                        _uiEvent.send(UiEvent.ShowSnackbar(
                            message = stringResourcesProvider.getString(R.string.scaffold_date_error)
                        ))
                        return@launch
                    } else if(selectedGoalAmount == 0){
                        _uiEvent.send(UiEvent.ShowSnackbar(
                            message = stringResourcesProvider.getString(R.string.goal_and_unit_error)
                        ))
                        return@launch

                    } else {

                        if(habit == null){


                            val rowId = repository.insertHabit(
                                Habit(
                                name = this@HabitConfigurationViewModel.name,
                                icon = this@HabitConfigurationViewModel.selectedIcon,
                                startDate = this@HabitConfigurationViewModel.selectedStartDate,
                                endDate = this@HabitConfigurationViewModel.selectedEndDate,
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

                            if(habit?.startDate!! > selectedStartDate){


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


                                val startDate: LocalDate = habit?.startDate!!
                                val endDate: LocalDate = selectedStartDate!!
                                var currentDate: LocalDate = startDate

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


                            repository.insertHabit(
                                Habit(
                                name = this@HabitConfigurationViewModel.name,
                                icon = this@HabitConfigurationViewModel.selectedIcon,
                                startDate = this@HabitConfigurationViewModel.selectedStartDate,
                                endDate = this@HabitConfigurationViewModel.selectedEndDate,
                                habitType = this@HabitConfigurationViewModel.habitType,
                                goalAmount = this@HabitConfigurationViewModel.selectedGoalAmount,
                                unitType = this@HabitConfigurationViewModel.selectedUnitType,
                                id = habit?.id)
                            )

                        }

                        _uiEvent.send(UiEvent.PopBack)
                    }

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
                updateSelectedElementInListOfOptions(unitTypeOptions ,selectedUnitTypeIndex, false)
                selectedUnitTypeIndex = event.index
                selectedUnitType = UnitType.entries.find { it.value == selectedUnitTypeIndex }?.toString() ?: ""
                updateSelectedElementInListOfOptions(unitTypeOptions, selectedUnitTypeIndex, true)
            }

            else -> {}
        }
    }

}

enum class UnitType(var value: Int) {
    km(0),
    hours(1)
}
