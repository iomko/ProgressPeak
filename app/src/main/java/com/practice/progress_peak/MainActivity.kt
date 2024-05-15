package com.practice.progress_peak

import com.practice.progress_peak.screens.HabitConfiguration.MainComponents.HabitConfigurationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.practice.progress_peak.screens.HabitProgress.MainComponents.HabitProgressScreen
import com.practice.progress_peak.screens.HabitStatistics.MainComponents.HabitStatisticsScreen
import com.practice.progress_peak.screens.MainHabitList.MainComponents.MainHabitListScreen
import com.practice.progress_peak.ui.theme.Progress_PeakTheme
import com.practice.progress_peak.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Progress_PeakTheme {
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = Routes.MAIN_SCREEN){

                    composable(Routes.MAIN_SCREEN){
                        MainHabitListScreen(navigate = {navController.navigate(it.route)})
                    }

                    composable(Routes.CONFIGURATION_SCREEN + "?habitId={habitId}",
                        arguments = listOf(
                            navArgument(name = "habitId"){
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )){
                        HabitConfigurationScreen(popBack = { navController.popBackStack() })

                    }

                    composable(Routes.PROGRESS_SCREEN + "/?habitId={habitId}/?habitProgressId={habitProgressId}",
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

                    composable(Routes.STATISTICS_SCREEN){
                        HabitStatisticsScreen(navigate = {navController.navigate(it.route)})
                    }

                }

            }
        }
    }
}
