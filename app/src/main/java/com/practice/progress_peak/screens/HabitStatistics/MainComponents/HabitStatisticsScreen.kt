package com.practice.progress_peak.screens.HabitStatistics.MainComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListSelection
import com.practice.progress_peak.screens.BottomBarNavigation
import com.practice.progress_peak.screens.HabitStatistics.OtherComponents.HabitStatisticsBoxInfo
import com.practice.progress_peak.screens.HabitStatistics.ScaffoldBars.HabitStatisticsTopBar
import com.practice.progress_peak.utils.UiEvent
import com.practice.progress_peak.utils.FunctionExtensions.formatCount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitStatisticsScreen(
    navigate: (UiEvent.Navigate) -> Unit,
    viewModel: HabitStatisticsViewModel = hiltViewModel()
) {

    var temporarySelectedHabitIndex = viewModel.selectedHabitIndex

    val dismissAndCloseHabitSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitStatisticsEvent.ExpandHabits(false))
    }

    val changeHabitSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitStatisticsEvent.ChangeHabit(temporarySelectedHabitIndex))
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
        bottomBar = {
            BottomBarNavigation(onClickHomeIcon = { (viewModel::onEvent)(HabitStatisticsEvent.EnterMainHabitListScreen) })
        },
        topBar = {
            HabitStatisticsTopBar(viewModel.name, onIconClick = { (viewModel::onEvent)(
                HabitStatisticsEvent.ExpandHabits(true)
            ) })
        }
    ){ paddingValues ->
        if(viewModel.habitNamesList.isNotEmpty()){
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.DarkGray)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(viewModel.statisticsBoxes) { box ->
                        HabitStatisticsBoxInfo(
                            icon = box.icon,
                            textLeft = box.count.formatCount(),
                            textRight = box.text
                        )
                    }
                }
            }

            if(viewModel.expandedHabit){
                ListDialog(
                    state = rememberUseCaseState(visible = true, onCloseRequest = { dismissAndCloseHabitSelection() }, onFinishedRequest = {
                        changeHabitSelection()
                        dismissAndCloseHabitSelection()
                    }, onDismissRequest = {
                        dismissAndCloseHabitSelection()
                    }),
                    selection = ListSelection.Single(
                        options = viewModel.habitNamesList
                    ) { index, _ ->
                        temporarySelectedHabitIndex = index
                    }
                )
            }
        }

    }

}

