package com.jams.totodayalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(task: Task) {
        if (task.reminderTime == null) return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", task.description)
            putExtra("EXTRA_TITLE", task.title)
            putExtra("EXTRA_ID", task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // Agenda o alarme para o horário exato, mesmo em modo Doze
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                task.reminderTime,
                pendingIntent
            )
            Log.d("AlarmScheduler", "Alarme agendado com sucesso para: ${task.reminderTime} (Task ID: ${task.title})")
        } catch (e: SecurityException) {
            Log.e("AlarmScheduler", "Erro de permissão ao agendar alarme exato: ${e.message}")
            e.printStackTrace()
        } catch (e: Exception) {
            Log.e("AlarmScheduler", "Erro desconhecido ao agendar: ${e.message}")
        }
    }

    fun cancel(task: Task) {
        try {
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                task.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            Log.d("AlarmScheduler", "Alarme cancelado para a tarefa: ${task.title}")
        } catch (e: Exception) {
            Log.e("AlarmScheduler", "Erro ao cancelar alarme: ${e.message}")
        }
    }
}