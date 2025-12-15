package com.jams.totodayalarm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

enum class SortOption(val label: String) {
    TITLE_ASC("Ordem A-Z"),
    TITLE_DESC("Ordem Z-A"),
    COMPLETED_FIRST("Concluídas Primeiro"),
    PENDING_FIRST("Pendentes Primeiro")
}

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val _tasks = mutableStateListOf<Task>()
    private val scheduler = AlarmScheduler(application)

    var searchQuery by mutableStateOf("")
        private set

    var currentSortOption by mutableStateOf(SortOption.PENDING_FIRST)
        private set

    val filteredTasks: List<Task>
        get() {
            val filtered = if (searchQuery.isBlank()) {
                _tasks
            } else {
                _tasks.filter { it.title.contains(searchQuery, ignoreCase = true) }
            }

            return when (currentSortOption) {
                SortOption.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
                SortOption.TITLE_DESC -> filtered.sortedByDescending { it.title.lowercase() }
                SortOption.COMPLETED_FIRST -> filtered.sortedByDescending { it.isCompleted }
                SortOption.PENDING_FIRST -> filtered.sortedBy { it.isCompleted }
            }
        }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun onSortOptionChange(option: SortOption) {
        currentSortOption = option
    }

    // Adiciona tarefa com agendamento opcional
    fun addTask(title: String, description: String, reminderTime: Long? = null) {
        if (title.isNotBlank()) {
            val newTask = Task(title = title, description = description, reminderTime = reminderTime)
            _tasks.add(newTask)

            if (reminderTime != null) {
                scheduler.schedule(newTask)
            }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        val index = _tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            val updatedTask = _tasks[index].copy(isCompleted = !task.isCompleted)
            _tasks[index] = updatedTask

            // Lógica opcional: cancelar alarme se completada
            if (updatedTask.isCompleted) {
                scheduler.cancel(updatedTask)
            } else if (updatedTask.reminderTime != null && updatedTask.reminderTime > System.currentTimeMillis()) {
                // Se desmarcar e o tempo for futuro, agenda novamente
                scheduler.schedule(updatedTask)
            }
        }
    }

    // Atualiza tarefa e reagenda alarme
    fun updateTask(id: String, newTitle: String, newDescription: String, newReminderTime: Long?) {
        val index = _tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            val oldTask = _tasks[index]
            scheduler.cancel(oldTask) // Cancela o antigo

            val updatedTask = oldTask.copy(
                title = newTitle,
                description = newDescription,
                reminderTime = newReminderTime
            )
            _tasks[index] = updatedTask

            if (newReminderTime != null) {
                scheduler.schedule(updatedTask)
            }
        }
    }

    fun removeTask(task: Task) {
        scheduler.cancel(task)
        _tasks.removeIf { it.id == task.id }
    }

    fun getTaskById(id: String): Task? {
        return _tasks.find { it.id == id }
    }
}