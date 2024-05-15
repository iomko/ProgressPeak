package com.practice.progress_peak.screens.MainHabitList.ScaffoldBars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TopBarCalendar(currentDate: LocalDate,
                   currentSelectedDate: LocalDate,
                   onClickPreviousWeek: () -> Unit,
                   onClickNextWeek: () -> Unit,
                   onPickDate: (LocalDate) -> Unit
                   ) {
    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "${currentDate.year}-${currentDate.month}",
                    style = TextStyle(fontSize = 18.sp),
                    color = Color.White
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                IconButton(onClick = {
                    onClickPreviousWeek()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous Week", tint = Color.White)
                }
                LazyRow {

                    repeat(7) { index ->
                        val day = currentDate.plusDays(index.toLong())
                        item {
                            DayItem(
                                day = day,
                                isSelected = day == currentSelectedDate,
                                onClick = { onPickDate(day) }
                            )
                        }
                    }
                }
                IconButton(onClick = {
                    onClickNextWeek()
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next Week", tint = Color.White)
                }
            }
        }
    }

}

@Composable
fun DayItem(day: LocalDate, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(255, 165, 0) else Color.Transparent

    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .background(
                color = backgroundColor
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(day.format(DateTimeFormatter.ofPattern("E")), color = Color.White)
            Text(day.dayOfMonth.toString(), color = Color.White)
        }
    }
}
