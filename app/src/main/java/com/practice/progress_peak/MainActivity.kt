package com.practice.progress_peak

import HabitConfigurationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.practice.progress_peak.screens.HabitProgress.HabitProgressScreen
import com.practice.progress_peak.screens.MainHabitList.MainHabitListScreen
import com.practice.progress_peak.ui.theme.Progress_PeakTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Progress_PeakTheme {
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = "main_habit_list_screen"){

                    composable("main_habit_list_screen"){
                        MainHabitListScreen(navigate = {navController.navigate(it.route)})
                    }

                    composable("habit_configuration_screen" + "?habitId={habitId}",
                        arguments = listOf(
                            navArgument(name = "habitId"){
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )){
                        HabitConfigurationScreen(popBack = { navController.popBackStack() })

                    }

                    composable("habit_progress_screen" + "/?habitId={habitId}/?habitProgressId={habitProgressId}",
                        arguments = listOf(
                            navArgument(name = "habitId") {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(name = "habitProgressId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        HabitProgressScreen(popBack = { navController.popBackStack() })
                    }

                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Progress_PeakTheme {
        Greeting("Android")
    }
}