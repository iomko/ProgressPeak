package com.practice.progress_peak.screens.HabitStatistics.ScaffoldBars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.practice.progress_peak.R

@Composable
fun HabitStatisticsTopBar(
    habitName: String,
    onIconClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxHeight()
                .background(colorResource(R.color.orange)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.statistics_screen_bar),
                color = colorResource(R.color.black),
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier.weight(1f).fillMaxHeight()
                .background(colorResource(R.color.light_gray)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = habitName,
                    color = colorResource(R.color.black),
                    modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                )
                IconButton(
                    onClick = onIconClick,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                }
            }
        }
    }
}