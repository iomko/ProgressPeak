package com.practice.progress_peak.screens.MainHabitList.MainComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListSelection
import com.practice.progress_peak.R
import com.practice.progress_peak.screens.BottomBarNavigation
import com.practice.progress_peak.screens.MainHabitList.OtherComponents.BadGoodHabitSelectionBar
import com.practice.progress_peak.screens.MainHabitList.OtherComponents.CustomHabitItem
import com.practice.progress_peak.screens.MainHabitList.OtherComponents.DropDownFilterOption
import com.practice.progress_peak.screens.MainHabitList.ScaffoldBars.TopBarCalendar
import com.practice.progress_peak.utils.UiEvent
import com.practice.progress_peak.utils.getStringNameFromListOfOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHabitListScreen(
    navigate: (UiEvent.Navigate) -> Unit,
    viewModel: MainHabitListViewModel = hiltViewModel()
)
{

    var temporarySelectedSortByTypeIndex = viewModel.currentSortTypeIndex

    var temporarySelectedOrderingTypeIndex = viewModel.currentOrderingTypeIndex

    val habits = viewModel.habits.collectAsState().value

    val changeSortTypeSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(MainHabitListEvent.ChangeSortByType(temporarySelectedSortByTypeIndex))
    }

    val dismissAndCloseSortTypeSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(MainHabitListEvent.ExpandSortType(false))
    }

    val changeOrderingTypeSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(MainHabitListEvent.ChangeOrderingType(temporarySelectedOrderingTypeIndex))
    }

    val dismissAndCloseOrderingTypeSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(MainHabitListEvent.ExpandOrderingType(false))
    }


    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{ event ->
            when(event){
                is UiEvent.Navigate -> {
                    navigate(event)
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
             TopBarCalendar(
                 currentDate = viewModel.currentDate,
                 currentSelectedDate = viewModel.selectedDate,
                 onClickPreviousWeek = { (viewModel::onEvent)(MainHabitListEvent.GoToPreviousWeek) },
                 onClickNextWeek = { (viewModel::onEvent)(MainHabitListEvent.GoToNextWeek) },
                 onPickDate = { date -> (viewModel::onEvent)(MainHabitListEvent.PickDate(date)) }
             )
        },
        bottomBar = {
            BottomBarNavigation(onClickInfoIcon =
            {(viewModel::onEvent)(MainHabitListEvent.EnterStatisticsScreen)})
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(MainHabitListEvent.AddHabit)
            }){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            BadGoodHabitSelectionBar(
                currentSelectedHabitType = viewModel.currentHabitType,
                onClickGoodHabit = { viewModel.onEvent(
                    MainHabitListEvent.ChangeHabitType(
                        HabitType.Good
                    )
                ) },
                onClickBadHabit = { viewModel.onEvent(
                    MainHabitListEvent.ChangeHabitType(
                        HabitType.Bad
                    )
                ) })

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                DropDownFilterOption(
                    columnText = stringResource(R.string.filter_sorting),
                    currentSelectedType = getStringNameFromListOfOptions(viewModel.sortListOptions,viewModel.currentSortTypeIndex),
                    onExpandedColumn = { expandSort -> viewModel.onEvent(MainHabitListEvent.ExpandSortType(expandSort)) },
                )

                DropDownFilterOption(
                    columnText = stringResource(R.string.filter_ordering),
                    currentSelectedType = getStringNameFromListOfOptions(viewModel.orderingListOptions,viewModel.currentOrderingTypeIndex),
                    onExpandedColumn = { expandOrdering -> viewModel.onEvent(MainHabitListEvent.ExpandOrderingType(expandOrdering)) },
                )

                if(viewModel.expandedSortType){
                    ListDialog(
                        state = rememberUseCaseState(visible = true, onCloseRequest = { dismissAndCloseSortTypeSelection() }, onFinishedRequest = {
                            changeSortTypeSelection()
                            dismissAndCloseSortTypeSelection()
                        }, onDismissRequest = {
                            dismissAndCloseSortTypeSelection()
                        }),
                        selection = ListSelection.Single(
                            showRadioButtons = true,
                            options = viewModel.sortListOptions,
                        ) { index, _ ->
                            temporarySelectedSortByTypeIndex = index
                        }
                    )
                }

                if(viewModel.expandedOrderingType){
                    ListDialog(
                        state = rememberUseCaseState(visible = true, onCloseRequest = { dismissAndCloseOrderingTypeSelection() }, onFinishedRequest = {
                            changeOrderingTypeSelection()
                            dismissAndCloseOrderingTypeSelection()
                        }, onDismissRequest = { dismissAndCloseOrderingTypeSelection() }),
                        selection = ListSelection.Single(
                            showRadioButtons = true,
                            options = viewModel.orderingListOptions
                        ) { index, _ ->
                            temporarySelectedOrderingTypeIndex = index
                        }
                    )
                }

            }

            LazyColumn {
                items(habits) { habit ->
                    CustomHabitItem(
                        habit = habit.first,
                        habitProgression = habit.second,
                        onHabitClick = { (viewModel::onEvent)(MainHabitListEvent.EditHabitProgress(habit.first, habit.second)) },
                        onSettingsClick = { (viewModel::onEvent)(MainHabitListEvent.EditHabit(habit.first)) }
                    )
                }
            }
        }
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
