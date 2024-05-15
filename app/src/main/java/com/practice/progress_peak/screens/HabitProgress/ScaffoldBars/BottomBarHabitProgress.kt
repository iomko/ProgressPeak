package com.practice.progress_peak.screens.HabitProgress.ScaffoldBars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.progress_peak.R

@Composable
fun BottomBarHabitProgress(
    onDoneClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust padding
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.orange))
            .clickable { onDoneClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.habit_progress_modification_done),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp),
            fontSize = 24.sp
        )
    }
}