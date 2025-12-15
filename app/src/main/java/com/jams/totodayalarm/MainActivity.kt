package com.jams.totodayalarm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jams.totodayalarm.ui.theme.ToTodayAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Criar o canal de notificação logo no início
        createNotificationChannel()

        setContent {
            ToTodayAlarmTheme {
                val navController = rememberNavController()
                val todoViewModel: TodoViewModel = viewModel()
                val context = LocalContext.current

                // 2. Pedir permissão de Notificação (Android 13+)
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (!isGranted) {
                            Toast.makeText(context, "Notificações são necessárias para os lembretes.", Toast.LENGTH_LONG).show()
                        }
                    }
                )

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lembretes de Tarefas"
            val descriptionText = "Canal para notificações de lembretes"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("todo_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}