package com.practice.progress_peak.screens.HabitConfiguration.OtherComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IconRow(icon: String, onIconButtonClick: () -> Unit) {
    GeneralTextWithContent(text = "Icon:") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            OutlinedButton(
                onClick = { onIconButtonClick() },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = icon)
            }
        }
    }
}