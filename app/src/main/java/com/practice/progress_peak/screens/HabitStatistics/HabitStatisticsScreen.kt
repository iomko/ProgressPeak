package com.practice.progress_peak.screens.HabitStatistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListOption
import com.maxkeppeler.sheets.list.models.ListSelection
import com.practice.progress_peak.screens.HabitProgress.HabitProgressEvent
import com.practice.progress_peak.screens.MainHabitList.MainHabitListEvent
import com.practice.progress_peak.utils.UiEvent

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(255, 165, 0)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (viewModel::onEvent)(HabitStatisticsEvent.EnterMainHabitListScreen) }) {
                    Icon(Icons.Default.Home, contentDescription = "", tint = Color.White)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Info, contentDescription = "", tint = Color.White)
                }
            }
        },
        topBar = {
            HabitStatisticsTopBar(viewModel.name, onIconClick = { (viewModel::onEvent)(
                HabitStatisticsEvent.ExpandHabits(true)) })
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
                        StatisticsBox(
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

fun Number.formatCount(): String {
    return when (this) {
        is Int -> toString()
        is Float, is Double -> "%.2f".format(this)
        else -> throw IllegalArgumentException("Unsupported number type")
    }
}

@Composable
fun StatisticsBox(icon: ImageVector, textLeft: String, textRight: String) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = textLeft,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = textRight,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun HabitStatisticsTopBar(
    habitName: String,
    onIconClick: () -> Unit,
) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Max),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight()
                    .background(Color(
                        255,
                        165,
                        0
                    )),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Statistics",
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = habitName,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                    )
                    IconButton(
                        onClick = onIconClick,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                    }
                }
            }
        }
}

