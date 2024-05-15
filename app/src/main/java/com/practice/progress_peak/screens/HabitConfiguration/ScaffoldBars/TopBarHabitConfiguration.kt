package com.practice.progress_peak.screens.HabitConfiguration.ScaffoldBars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.progress_peak.R

@Composable
fun TopBarHabitConfiguration(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.orange))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.habit_configuration_top_bar),
            color = colorResource(R.color.white),
            fontSize = 20.sp,
        )
    }
}
