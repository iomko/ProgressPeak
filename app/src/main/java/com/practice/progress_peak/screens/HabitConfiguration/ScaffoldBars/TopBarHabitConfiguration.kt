package com.practice.progress_peak.screens.HabitConfiguration.ScaffoldBars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBarHabitConfiguration(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(255, 165, 0))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Habit Configuration",
            color = Color.White,
            fontSize = 20.sp,
        )
    }
}
