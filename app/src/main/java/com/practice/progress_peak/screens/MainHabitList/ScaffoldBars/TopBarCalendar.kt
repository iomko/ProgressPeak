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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.progress_peak.R
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
                .background(colorResource(R.color.dark_gray))
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
                    color = colorResource(R.color.white)
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
                    Icon(Icons.Default.ArrowBack, contentDescription = "", tint = colorResource(R.color.white))
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
                    Icon(Icons.Default.ArrowForward, contentDescription = "", tint = colorResource(R.color.white))
                }
            }
        }
    }

}

@Composable
fun DayItem(day: LocalDate, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) colorResource(R.color.orange) else colorResource(R.color.transparent)

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
            Text(day.format(DateTimeFormatter.ofPattern("E")), color = colorResource(R.color.white))
            Text(day.dayOfMonth.toString(), color = colorResource(R.color.white))
        }
    }
}
