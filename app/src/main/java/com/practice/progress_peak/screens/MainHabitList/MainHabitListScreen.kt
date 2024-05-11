package com.practice.progress_peak.screens.MainHabitList

import DayItem
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import com.practice.progress_peak.utils.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHabitListScreen(
    navigate: (UiEvent.Navigate) -> Unit,
    viewModel: MainHabitListViewModel = hiltViewModel()
)
{
    val context = LocalContext.current

    val habits = viewModel.habits.collectAsState(initial = emptyList())

    var sortByText by remember { mutableStateOf("Sort by") }

    var expanded by remember { mutableStateOf(false) }

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
                // Add your icon buttons here
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
            // Row with two rounded rectangles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
            ) {
                // Rounded rectangle 1 (Good Habit)
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

                // Rounded rectangle 2 (Bad Habit)
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

            // Row with sort dropdown
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                DropDownOption(
                    columnText = "Sort by",
                    currentSelectedType = viewModel.currentSortType,
                    onChangeType = { sortType -> viewModel.onEvent(MainHabitListEvent.ChangeSortByType(sortType)) },
                    isExpanded = viewModel.expandedSortType,
                    onExpandedColumn = { expandSort -> viewModel.onEvent(MainHabitListEvent.ExpandSortType(expandSort)) },
                    options = viewModel.sortListOptions
                )

                DropDownOption(
                    columnText = "Categories",
                    currentSelectedType = viewModel.currentCategoriesType,
                    onChangeType = { categoryType -> viewModel.onEvent(MainHabitListEvent.ChangeCategoriesType(categoryType)) },
                    isExpanded = viewModel.expandedCategoriesType,
                    onExpandedColumn = { expandCategory -> viewModel.onEvent(MainHabitListEvent.ExpandCategoriesType(expandCategory)) },
                    options = viewModel.categoriesListOptions
                )

                DropDownOption(
                    columnText = "Tags",
                    currentSelectedType = viewModel.currentTagsType,
                    onChangeType = { tagType -> viewModel.onEvent(MainHabitListEvent.ChangeTagsType(tagType)) },
                    isExpanded = viewModel.expandedTagsType,
                    onExpandedColumn = { expandTag -> viewModel.onEvent(MainHabitListEvent.ExpandTagsType(expandTag)) },
                    options = viewModel.tagsListOptions
                )

            }

            LazyColumn(
                modifier = Modifier.weight(1f) // Occupy remaining space
            ) {
                items(habits.value) { habit ->
                    HabitItem(
                        name = habit.name,
                        icon = habit.icon,
                        completed = habit.completed,
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
