package com.practice.progress_peak.screens.HabitProgress.ScaffoldBars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.progress_peak.R

@Composable
fun TopBarHabitProgress(
    habitName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(color = colorResource(R.color.orange))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.habit_progress_top_bar),
                color = colorResource(R.color.black),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(color = colorResource(R.color.light_gray))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = habitName,
                color = colorResource(R.color.black),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}