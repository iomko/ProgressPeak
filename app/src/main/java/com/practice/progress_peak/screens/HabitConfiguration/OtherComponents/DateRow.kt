package com.practice.progress_peak.screens.HabitConfiguration.OtherComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.practice.progress_peak.R

@Composable
fun DateRow(
    nameText: String,
    buttonText: String,
    onButtonClick: () -> Unit,
) {
    GeneralTextWithContent(text = nameText) {
        OutlinedButton(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            border = BorderStroke(2.dp, colorResource(R.color.orange)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = buttonText)
        }
    }
}