package com.jams.totoday

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Enum para definir os tipos de ordenação
enum class SortOption(val label: String) {
    TITLE_ASC("Ordem A-Z"),
    TITLE_DESC("Ordem Z-A"),
    COMPLETED_FIRST("Concluídas Primeiro"),
    PENDING_FIRST("Pendentes Primeiro")
}

class TodoViewModel : ViewModel() {
    // Lista "Fonte da Verdade" - contém todos os dados
    private val _tasks = mutableStateListOf<Task>()

    // Estados para Pesquisa e Ordenação
    var searchQuery by mutableStateOf("")
        private set

    var currentSortOption by mutableStateOf(SortOption.PENDING_FIRST)
        private set

    // Lista filtrada e ordenada para a UI exibir
    // O Compose rastreia as leituras de _tasks, searchQuery e currentSortOption aqui
    val filteredTasks: List<Task>
        get() {
            // 1. Filtrar por nome (pesquisa)
            val filtered = if (searchQuery.isBlank()) {
                _tasks
            } else {
                _tasks.filter { it.title.contains(searchQuery, ignoreCase = true) }
            }

            // 2. Aplicar ordenação
            return when (currentSortOption) {
                SortOption.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
                SortOption.TITLE_DESC -> filtered.sortedByDescending { it.title.lowercase() }
                // true (concluída) > false, então descending coloca true primeiro
                SortOption.COMPLETED_FIRST -> filtered.sortedByDescending { it.isCompleted }
                // false (pendente) < true, então ascending coloca false primeiro
                SortOption.PENDING_FIRST -> filtered.sortedBy { it.isCompleted }
            }
        }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun onSortOptionChange(option: SortOption) {
        currentSortOption = option
    }

    fun addTask(title: String, description: String) {
        if (title.isNotBlank()) {
            // Adiciona sempre na lista original
            _tasks.add(Task(title = title, description = description))
        }
    }

    fun toggleTaskCompletion(task: Task) {
        val index = _tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            val updatedTask = _tasks[index].copy(isCompleted = !task.isCompleted)
            _tasks[index] = updatedTask
        }
    }

    fun updateTask(id: String, newTitle: String, newDescription: String) {
        val index = _tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            val currentTask = _tasks[index]
            _tasks[index] = currentTask.copy(title = newTitle, description = newDescription)
        }
    }

    fun removeTask(task: Task) {
        _tasks.removeIf { it.id == task.id }
    }

    fun getTaskById(id: String): Task? {
        return _tasks.find { it.id == id }
    }
}