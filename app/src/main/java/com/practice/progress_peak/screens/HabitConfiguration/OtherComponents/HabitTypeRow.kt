package com.practice.progress_peak.screens.HabitConfiguration.OtherComponents

import androidx.compose.foundation.background
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
import com.practice.progress_peak.screens.MainHabitList.MainComponents.HabitType

@Composable
fun HabitTypeRow(
    habitType: Boolean,
    onHabitTypeSelected: (HabitType) -> Unit
) {
    val selectedColor = Color(255, 165, 0)
    val unselectedColor = Color.Transparent

    GeneralTextWithContent(text = "Habit type:") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .clickable { onHabitTypeSelected(HabitType.Good) }
                    .background(
                        color = if (habitType == HabitType.Good.value) selectedColor else unselectedColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Good habit"
                )
            }

            Box(
                modifier = Modifier
                    .clickable { onHabitTypeSelected(HabitType.Bad) }
                    .background(
                        color = if (habitType == HabitType.Bad.value) selectedColor else unselectedColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Bad habit"
                )
            }
        }
    }
}