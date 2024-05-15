package com.practice.progress_peak.screens.MainHabitList.OtherComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropDownFilterOption(
    columnText: String,
    currentSelectedType: String,
    onExpandedColumn: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = columnText,
            fontSize = 16.sp
        )

        Box(
            modifier = Modifier
                .padding(2.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clickable { onExpandedColumn(true) }
        ) {

            Text(
                text = currentSelectedType,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}