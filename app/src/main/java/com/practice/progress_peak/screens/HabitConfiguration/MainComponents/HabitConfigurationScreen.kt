package com.practice.progress_peak.screens.HabitConfiguration.MainComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.maxkeppeker.sheets.core.icons.LibIcons
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.emoji.EmojiDialog
import com.maxkeppeler.sheets.emoji.models.EmojiConfig
import com.maxkeppeler.sheets.emoji.models.EmojiSelection
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListSelection
import com.practice.progress_peak.R
import com.practice.progress_peak.screens.HabitConfiguration.ScaffoldBars.BottomBarHabitConfiguration
import com.practice.progress_peak.screens.HabitConfiguration.OtherComponents.DateRow
import com.practice.progress_peak.screens.HabitConfiguration.OtherComponents.GoalAndUnitTypeRow
import com.practice.progress_peak.screens.HabitConfiguration.OtherComponents.HabitTypeRow
import com.practice.progress_peak.screens.HabitConfiguration.OtherComponents.IconRow
import com.practice.progress_peak.screens.HabitConfiguration.OtherComponents.NameRow
import com.practice.progress_peak.screens.HabitConfiguration.ScaffoldBars.TopBarHabitConfiguration
import com.practice.progress_peak.utils.UiEvent
import com.practice.progress_peak.utils.getStringNameFromListOfOptions
import kotlinx.coroutines.launch


//Spôsob inicializácie okien pomocou Dagger Hilt dependency injection knižnice som robil na základe
//tutoriálu: https://www.youtube.com/watch?v=A7CGcFjQQtQ&t=3102s
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitConfigurationScreen(
    popBack: () -> Unit,
    viewModel: HabitConfigurationViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val snackBarScope = rememberCoroutineScope()

    var temporarySelectedUnitTypeIndex = viewModel.selectedUnitTypeIndex

    val dismissAndCloseUnitTypeSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ExpandUnitType(false))
    }

    val changeUnitTypeSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ChangeUnitType(temporarySelectedUnitTypeIndex))
    }

    var temporarySelectedStartDate = viewModel.selectedStartDate

    val dismissAndCloseStartDateSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ExpandStartDate(false))
    }

    val changeStartDateSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ChangeStartDate(temporarySelectedStartDate))
    }

    var temporarySelectedEndDate = viewModel.selectedStartDate

    val dismissAndCloseEndDateSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ExpandEndDate(false))
    }

    val changeEndDateSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ChangeEndDate(temporarySelectedEndDate))
    }

    var temporarySelectedIcon = viewModel.selectedIcon


    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{ event ->
            when(event){
                is UiEvent.PopBack -> {
                    popBack()
                }
                is UiEvent.ShowSnackbar -> {
                    snackBarScope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopBarHabitConfiguration()
        },
        bottomBar = {
            BottomBarHabitConfiguration(
                onClickAddHabit = { (viewModel::onEvent)(HabitConfigurationEvent.AddHabit) },
                onClickCancelHabit = { (viewModel::onEvent)(HabitConfigurationEvent.CancelHabit) },
                onClickDeleteHabit = { (viewModel::onEvent)(HabitConfigurationEvent.DeleteHabit) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            NameRow(viewModel.name) { newName ->
                viewModel.onEvent(HabitConfigurationEvent.ChangeName(newName))
            }

            IconRow(
                icon = viewModel.selectedIcon,
                onIconButtonClick = { (viewModel::onEvent)(HabitConfigurationEvent.ExpandIcon(true)) }
            )

            if(viewModel.expandedIcon){
                EmojiDialog(
                    state = rememberUseCaseState(visible = true, onCloseRequest = {
                        (viewModel::onEvent)(HabitConfigurationEvent.ExpandIcon(false))
                    },
                        onDismissRequest = { (viewModel::onEvent)(HabitConfigurationEvent.ExpandIcon(false)) },
                        onFinishedRequest = { (viewModel::onEvent)(HabitConfigurationEvent.ChangeIcon(temporarySelectedIcon)) }),
                    selection = EmojiSelection.Unicode(
                        onPositiveClick = { emojiUnicode ->
                            temporarySelectedIcon = emojiUnicode
                        }
                    ),
                    config = EmojiConfig(icons = LibIcons.TwoTone)
                )
            }

            HabitTypeRow(viewModel.habitType) { habitType ->
                (viewModel::onEvent)(HabitConfigurationEvent.ChangeHabitType(habitType))
            }

            DateRow(
                buttonText = viewModel.selectedStartDate.toString(),
                onButtonClick = {
                    viewModel.onEvent(HabitConfigurationEvent.ExpandStartDate(true))
                },
                nameText = stringResource(R.string.date_row_start)
            )

            DateRow(
                buttonText = viewModel.selectedEndDate.toString(),
                onButtonClick = {
                    viewModel.onEvent(HabitConfigurationEvent.ExpandEndDate(true))
                },
                nameText = stringResource(R.string.date_row_end)
            )

            if(viewModel.expandEndDate){
                CalendarDialog(
                    state = rememberUseCaseState(visible = true, true,
                        onCloseRequest = dismissAndCloseEndDateSelection,
                        onDismissRequest = dismissAndCloseEndDateSelection,
                        onFinishedRequest = changeEndDateSelection
                    ),
                    config = CalendarConfig(
                        yearSelection = true,
                        style = CalendarStyle.WEEK,
                    ),
                    selection = CalendarSelection.Date(
                        selectedDate = viewModel.selectedEndDate
                    ) { newDate ->
                        temporarySelectedEndDate = newDate
                    },
                )
            }

            if(viewModel.expandStartDate){

                CalendarDialog(
                    state = rememberUseCaseState(visible = true, true,
                        onCloseRequest = dismissAndCloseStartDateSelection,
                        onDismissRequest = dismissAndCloseStartDateSelection,
                        onFinishedRequest = changeStartDateSelection
                    ),
                    config = CalendarConfig(
                        yearSelection = true,
                        style = CalendarStyle.WEEK,
                    ),
                    selection = CalendarSelection.Date(
                        selectedDate = viewModel.selectedStartDate
                    ) { newDate ->
                        temporarySelectedStartDate = newDate
                    },
                )

            }

            GoalAndUnitTypeRow(
                goal = viewModel.selectedGoalAmount,
                onGoalChange = { newValue -> (viewModel::onEvent)(HabitConfigurationEvent.ChangeGoalAmount(newValue)) },
                unitType = getStringNameFromListOfOptions(viewModel.unitTypeOptions,viewModel.selectedUnitTypeIndex),
                onUnitButtonClick = { (viewModel::onEvent)(HabitConfigurationEvent.ExpandUnitType(true)) }
            )

            if(viewModel.expandUnitType){

                ListDialog(
                    state = rememberUseCaseState(visible = true, onCloseRequest = dismissAndCloseUnitTypeSelection,
                        onDismissRequest = dismissAndCloseUnitTypeSelection, onFinishedRequest = changeUnitTypeSelection),
                    selection = ListSelection.Single(
                        showRadioButtons = true,
                        options = viewModel.unitTypeOptions
                    ) { index, _ ->
                        temporarySelectedUnitTypeIndex = index
                    }
                )
            }

        }
    }
}





