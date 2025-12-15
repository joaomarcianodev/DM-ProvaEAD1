package com.jams.totoday

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(), // ID único para cada tarefa
    val title: String,
    val description: String,
    var isCompleted: Boolean = false
)