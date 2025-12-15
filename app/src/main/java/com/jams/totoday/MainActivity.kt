package com.jams.totoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jams.totoday.ui.theme.ToTodayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToTodayTheme {
                val navController = rememberNavController()
                val todoViewModel: TodoViewModel = viewModel()

                // Configuração para remover animações de transição
                NavHost(
                    navController = navController,
                    startDestination = "list",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }
                ) {
                    composable("list") {
                        TodoListScreen(
                            viewModel = todoViewModel,
                            onNavigateToDetail = { taskId ->
                                navController.navigate("detail/$taskId")
                            }
                        )
                    }
                    composable("detail/{taskId}") { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")
                        TodoDetailScreen(
                            taskId = taskId,
                            viewModel = todoViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}