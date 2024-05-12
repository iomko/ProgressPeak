package com.practice.progress_peak.screens.MainHabitList

import DayItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListSelection
import com.practice.progress_peak.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHabitListScreen(
    navigate: (UiEvent.Navigate) -> Unit,
    viewModel: MainHabitListViewModel = hiltViewModel()
)
{

    var temporarySelectedSortByTypeIndex = viewModel.currentSortTypeIndex

    var temporarySelectedOrderingTypeIndex = viewModel.currentOrderingTypeIndex

    val context = LocalContext.current

    //val habits = viewModel.habits.collectAsState(initial = emptyList())

    val habits = viewModel.habits.collectAsState().value

    var sortByText by remember { mutableStateOf("Sort by") }

    var expanded by remember { mutableStateOf(false) }

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
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "${viewModel.currentDate.year}-${viewModel.currentDate.month}",
                            style = TextStyle(fontSize = 18.sp),
                            color = Color.White
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        IconButton(onClick = {
                            viewModel.onEvent(MainHabitListEvent.GoToPreviousWeek)
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Week", tint = Color.White)
                        }
                        LazyRow {

                            repeat(7) { index ->
                                val day = viewModel.currentDate.plusDays(index.toLong())
                                item {
                                    DayItem(
                                        day = day,
                                        isSelected = day == viewModel.selectedDate,
                                        onClick = { viewModel.onEvent(MainHabitListEvent.PickDate(day)) }
                                    )
                                }
                            }
                        }
                        IconButton(onClick = {
                            viewModel.onEvent(MainHabitListEvent.GoToNextWeek)
                        }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Next Week", tint = Color.White)
                        }
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(255, 165, 0)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { /* Handle click */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Icon 1", tint = Color.White)
                }
                IconButton(onClick = { /* Handle click */ }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Icon 2", tint = Color.White)
                }
                IconButton(onClick = { /* Handle click */ }) {
                    Icon(Icons.Default.Build, contentDescription = "Icon 3", tint = Color.White)
                }
                IconButton(onClick = { /* Handle click */ }) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Icon 4", tint = Color.White)
                }
            }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
            ) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp)
                        .clickable(onClick = {
                            viewModel.onEvent(
                                MainHabitListEvent.ChangeHabitType(
                                    HabitType.Good
                                )
                            )
                        })
                        .background(
                            color = if (viewModel.currentHabitType == HabitType.Good) Color(
                                255,
                                165,
                                0
                            ) else Color.Transparent, shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Good Habits",
                        color = Color.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp)
                        .clickable(onClick = {
                            viewModel.onEvent(
                                MainHabitListEvent.ChangeHabitType(
                                    HabitType.Bad
                                )
                            )
                        })
                        .background(
                            color = if (viewModel.currentHabitType == HabitType.Bad) Color(
                                255,
                                165,
                                0
                            ) else Color.Transparent, shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bad Habits",
                        color = Color.Black
                    )
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                DropDownOption(
                    columnText = "Sort by",
                    currentSelectedType = viewModel.getStringNameFromIndex(viewModel.sortListOptions,viewModel.currentSortTypeIndex),
                    onExpandedColumn = { expandSort -> viewModel.onEvent(MainHabitListEvent.ExpandSortType(expandSort)) },
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

                DropDownOption(
                    columnText = "Ordering",
                    currentSelectedType = viewModel.getStringNameFromIndex(viewModel.orderingListOptions,viewModel.currentOrderingTypeIndex),
                    onExpandedColumn = { expandOrdering -> viewModel.onEvent(MainHabitListEvent.ExpandOrderingType(expandOrdering)) },
                )

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

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(habits) { habit ->
                    HabitItem(
                        name = habit.name,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(MainHabitListEvent.EditHabit(habit))
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }

}
