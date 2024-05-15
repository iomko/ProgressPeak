package com.practice.progress_peak.screens.HabitProgress.MainComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.progress_peak.screens.HabitProgress.OtherComponents.CircularProgressBar
import com.practice.progress_peak.screens.HabitProgress.OtherComponents.ProgressUpdatorBar
import com.practice.progress_peak.screens.HabitProgress.ScaffoldBars.BottomBarHabitProgress
import com.practice.progress_peak.screens.HabitProgress.ScaffoldBars.TopBarHabitProgress
import com.practice.progress_peak.utils.UiEvent

@Composable
fun HabitProgressScreen(
    popBack: () -> Unit,
    viewModel: HabitProgressViewModel = hiltViewModel()
){

    var inputAmount by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{ event ->
            when(event){
                is UiEvent.PopBack -> {
                    popBack()
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarHabitProgress(viewModel.name)
        },
        bottomBar = {
            BottomBarHabitProgress(onDoneClick = { (viewModel::onEvent)(HabitProgressEvent.Done) })
        }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,

            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // CircularProgressBar
                CircularProgressBar(
                    modifier = Modifier.padding(16.dp),
                    maxValue = viewModel.habitProgressionGoal,
                    currentAmount = viewModel.habitProgressionAmount,
                    progressColor = Color.Blue,
                    strokeWidth = 12.dp,
                    size = 200.dp,
                    textSize = 24f
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProgressUpdatorBar(
                        value = viewModel.currentInputAmount,
                        onValueChange = { newInput -> (viewModel::onEvent)(
                            HabitProgressEvent.ChangeInput(
                                newInput
                            )
                        ) },
                        onValueApply = { (viewModel::onEvent)(HabitProgressEvent.ApplyInput) },
                        onOperationChange = { (viewModel::onEvent)(HabitProgressEvent.ChangeOperation) },
                        currentOperation = viewModel.currentOperation
                    )

                }

            }
        }
    }

}

