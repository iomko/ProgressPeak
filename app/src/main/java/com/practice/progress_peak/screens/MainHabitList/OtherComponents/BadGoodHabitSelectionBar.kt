package com.practice.progress_peak.screens.MainHabitList.OtherComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.progress_peak.R
import com.practice.progress_peak.screens.MainHabitList.MainComponents.HabitType

@Composable
fun BadGoodHabitSelectionBar(
    currentSelectedHabitType: HabitType,
    onClickGoodHabit: () -> Unit,
    onClickBadHabit: () -> Unit
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.light_gray))
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(6.dp)
                .clickable(onClick = {
                    onClickGoodHabit()
                })
                .background(
                    color = if (currentSelectedHabitType == HabitType.Good) colorResource(R.color.orange) else colorResource(
                        R.color.transparent), shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.good_habits),
                color = colorResource(R.color.black)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(6.dp)
                .clickable(onClick = {
                    onClickBadHabit()
                })
                .background(
                    color = if (currentSelectedHabitType == HabitType.Bad) colorResource(R.color.orange) else colorResource(
                        R.color.transparent), shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.bad_habits),
                color = colorResource(R.color.black)
            )
        }
    }

}
