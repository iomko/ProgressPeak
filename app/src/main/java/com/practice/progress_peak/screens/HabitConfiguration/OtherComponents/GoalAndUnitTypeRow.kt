package com.practice.progress_peak.screens.HabitConfiguration.OtherComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.practice.progress_peak.R

@Composable
fun GoalAndUnitTypeRow(
    goal: Int,
    onGoalChange: (Int) -> Unit,
    unitType: String,
    onUnitButtonClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    GeneralTextWithContent(text = stringResource(R.string.goal_and_unit)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = goal.toString(),
                onValueChange = { newText ->
                    val newGoal = newText.takeWhile { it.isDigit() }.toIntOrNull() ?: 0
                    onGoalChange(newGoal)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            OutlinedButton(
                onClick = { onUnitButtonClick() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = unitType)
            }
        }
    }
}