package com.practice.progress_peak.screens.MainHabitList.OtherComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.progress_peak.R
import com.practice.progress_peak.data.Database.DatabaseTables.Habit
import com.practice.progress_peak.data.Database.DatabaseTables.HabitProgression

@Composable
fun CustomHabitItem(
    habit: Habit,
    habitProgression: HabitProgression,
    onHabitClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.light_gray))
            .clickable { onHabitClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp, end = 5.dp)
                ) {
                    Text(
                        text = habit.icon,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .align(Alignment.Center),
                        color = colorResource(R.color.black)
                    )
                }
                Text(
                    text = habit.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 5.dp),
                    color = colorResource(R.color.black)
                )
                Text(
                    text = "${habitProgression.progressToDate}/${habit.goalAmount} ${habit.unitType}",
                    modifier = Modifier
                        .widthIn(44.dp)
                        .padding(end = 10.dp),
                    textAlign = TextAlign.End,
                    color = colorResource(R.color.black)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp)
                        .clickable { onSettingsClick() },
                    tint = colorResource(id = R.color.black)
                )

                RectangularProgressBar(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    maxInt = habit.goalAmount,
                    currentInt = habitProgression.progressToDate,
                    backgroundColor = colorResource(R.color.dark_gray),
                    progressColor = colorResource(R.color.dark_blue),
                    height = 8.dp
                )
            }
        }
    }
}

@Composable
fun RectangularProgressBar(
    modifier: Modifier = Modifier,
    maxInt: Int,
    currentInt: Int,
    backgroundColor: Color = colorResource(R.color.light_gray),
    progressColor: Color = colorResource(R.color.dark_blue),
    height: Dp = 16.dp
) {
    val progressFraction = currentInt.toFloat() / maxInt.toFloat()
    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(RoundedCornerShape(percent = 50))
            .background(color = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progressFraction)
                .clip(RoundedCornerShape(percent = 50))
                .background(color = progressColor)
        )
    }
}