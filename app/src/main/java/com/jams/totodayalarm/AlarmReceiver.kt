package com.jams.totodayalarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarme disparado!") // <--- Verifique este log no Logcat

        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "Lembrete da tarefa!"
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "To Today"
        val taskId = intent.getStringExtra("EXTRA_ID")?.hashCode() ?: 0

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent para abrir o app ao clicar
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, "todo_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(taskId, notification)
            Log.d("AlarmReceiver", "Notificação enviada com sucesso ID: $taskId")
        } catch (e: Exception) {
            Log.e("AlarmReceiver", "Erro ao enviar notificação", e)
        }
    }
}