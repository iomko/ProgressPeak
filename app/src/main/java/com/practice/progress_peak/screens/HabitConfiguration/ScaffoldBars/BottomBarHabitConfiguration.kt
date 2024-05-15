package com.practice.progress_peak.screens.HabitConfiguration.ScaffoldBars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.progress_peak.R

@Composable
fun BottomBarHabitConfiguration(
    onClickAddHabit: () -> Unit,
    onClickCancelHabit: () -> Unit,
    onClickDeleteHabit: () -> Unit
){
    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onClickAddHabit() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.green)),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = stringResource(R.string.habit_configuration_apply), color = colorResource(R.color.white))
            }
            Button(
                onClick = { onClickCancelHabit() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_gray)),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = stringResource(R.string.habit_configuration_cancel), color = colorResource(R.color.white))
            }
        }

        Button(
            onClick = { onClickDeleteHabit() },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.red)),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.habit_configuration_delete), color = colorResource(R.color.white))
        }
    }

}