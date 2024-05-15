package com.practice.progress_peak.screens.HabitProgress.OtherComponents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.practice.progress_peak.screens.HabitProgress.MainComponents.Operation

@Composable
fun ProgressUpdatorBar(
    value: Int,
    onValueChange: (Int) -> Unit,
    onValueApply: () -> Unit,
    onOperationChange: () -> Unit,
    currentOperation: Operation
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    IconButton(
        onClick = { onOperationChange() }
    ) {
        Icon(
            imageVector = if (currentOperation == Operation.Adding) Icons.Default.KeyboardArrowUp
            else Icons.Default.KeyboardArrowDown,
            contentDescription = null
        )
    }
    Spacer(modifier = Modifier.width(8.dp))

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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}