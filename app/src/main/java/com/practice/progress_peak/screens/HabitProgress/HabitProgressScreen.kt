package com.practice.progress_peak.screens.HabitProgress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.progress_peak.screens.HabitConfiguration.HabitConfigurationEvent
import com.practice.progress_peak.screens.HabitConfiguration.HabitConfigurationViewModel
import com.practice.progress_peak.utils.UiEvent


@Composable
fun HabitProgressTopBar(
    habitName: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(color = Color(255, 165, 0))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Habit progress",
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(color = Color.LightGray)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = habitName,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
fun NumberField(
    value: Int,
    onValueChange: (Int) -> Unit,
    onValueApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = value.toString(),
        onValueChange = { it ->
            val newInput = it.takeWhile { it.isDigit() }.toIntOrNull() ?: 0
            onValueChange(newInput)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onValueApply()
            },
        ),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}


@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    maxValue: Int,
    currentAmount: Int,
    progressColor: Color = Color.Green,
    outlineColor: Color = Color.LightGray,
    strokeWidth: Dp = 8.dp,
    size: Dp = 100.dp,
    textSize: Float = 14f
) {
    val progress = if (maxValue != 0) currentAmount.toFloat() / maxValue.toFloat() else 0f


    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Draw light gray circular outline
        Canvas(modifier = Modifier.matchParentSize()) {
            val innerRadius = size.toPx() / 2 - strokeWidth.toPx() / 2
            val outerRadius = size.toPx() / 2 + strokeWidth.toPx() / 2
            val center = Offset(size.toPx() / 2, size.toPx() / 2)

            drawCircle(
                color = outlineColor,
                radius = size.toPx() / 2,
                style = Stroke(width = strokeWidth.toPx())
            )
        }

        // Draw progress arc
        Canvas(modifier = Modifier.matchParentSize()) {
            val innerRadius = size.toPx() / 2 - strokeWidth.toPx() / 2
            val outerRadius = size.toPx() / 2 + strokeWidth.toPx() / 2
            val center = Offset(size.toPx() / 2, size.toPx() / 2)

            drawArc(
                color = progressColor,
                startAngle = 270f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )
        }

        // Display text in the center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(progress * 100).toInt()}%",
                textAlign = TextAlign.Center,
                fontSize = textSize.sp
            )
            Text(
                text = "$currentAmount/$maxValue",
                textAlign = TextAlign.Center,
                fontSize = textSize.sp // Use sp extension property
            )
        }
    }
}



@Composable
fun HabitProgressScreen(
    popBack: () -> Unit,
    viewModel: HabitProgressViewModel = hiltViewModel()
){

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

    var inputAmount by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            HabitProgressTopBar(viewModel.name)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust padding
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(255, 165, 0))
                    .clickable { viewModel.onEvent(HabitProgressEvent.Done) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Done",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 24.sp
                )
            }
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
                    IconButton(
                        onClick = { viewModel.onEvent(HabitProgressEvent.ChangeOperation) }
                    ) {
                        Icon(
                            imageVector = if (viewModel.currentOperation == Operation.Adding) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }


                    Spacer(modifier = Modifier.width(8.dp))


                    NumberField(
                        value = viewModel.currentInputAmount,
                        onValueChange = { newInput -> (viewModel::onEvent)(HabitProgressEvent.ChangeInput(newInput)) },
                        onValueApply = { (viewModel::onEvent)(HabitProgressEvent.ApplyInput) },
                        modifier = Modifier.weight(1f)
                    )
                }

            }
        }
    }

}