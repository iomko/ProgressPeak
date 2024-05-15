package com.practice.progress_peak.screens.HabitConfiguration.OtherComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NameRow(name: String, onNameChange: (String) -> Unit) {
    GeneralTextWithContent(text = "Name:") {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            placeholder = { Text(text = "Enter name") },
            singleLine = true
        )
    }
}