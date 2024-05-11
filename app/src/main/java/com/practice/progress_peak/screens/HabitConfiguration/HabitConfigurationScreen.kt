package com.practice.progress_peak.screens.HabitConfiguration

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.progress_peak.utils.UiEvent

@Composable
fun HabitConfigurationScreen(
    popBack: () -> Unit,
    viewModel: HabitConfigurationViewModel = hiltViewModel()
){
    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{ event ->
            when(event){
                is UiEvent.PopBack -> {
                    popBack()
                }
                else -> Unit
            }
        }
    }


    Column {
        Text(text = "HabitConfigurationScreen")
    }
}