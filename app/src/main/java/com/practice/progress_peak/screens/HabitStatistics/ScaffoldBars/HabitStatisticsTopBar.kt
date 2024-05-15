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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
                .background(
                    Color(
                    255,
                    165,
                    0
                )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Statistics",
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier.weight(1f).fillMaxHeight()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = habitName,
                    color = Color.Black,
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