package com.jams.totodayalarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// --- Tela da Lista de Tarefas ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    // Estados do Formulário
    var showAddDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }
    var newTaskReminder by remember { mutableStateOf<Long?>(null) }

    // Estados dos Seletores
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    var showSortMenu by remember { mutableStateOf(false) }
    val titleFocusRequester = remember { FocusRequester() }

    // --- LÓGICA DE SELETORES ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showTimePicker = false
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val calendar = Calendar.getInstance()
                        val utcCalendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                        utcCalendar.timeInMillis = selectedDateMillis

                        calendar.set(
                            utcCalendar.get(Calendar.YEAR),
                            utcCalendar.get(Calendar.MONTH),
                            utcCalendar.get(Calendar.DAY_OF_MONTH),
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        calendar.set(Calendar.SECOND, 0)
                        newTaskReminder = calendar.timeInMillis
                    }
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    // --- DIALOG DE NOVA TAREFA ---
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Nova Tarefa") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        label = { Text("Título") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(titleFocusRequester),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newTaskDescription,
                        onValueChange = { newTaskDescription = it },
                        label = { Text("Descrição") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão/Exibição do Lembrete
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (newTaskReminder != null) {
                            val formatter = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                            val dateString = formatter.format(java.util.Date(newTaskReminder!!))

                            Icon(Icons.Default.Alarm, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(dateString, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { newTaskReminder = null }) {
                                Icon(Icons.Default.Clear, contentDescription = "Remover")
                            }
                        } else {
                            OutlinedButton(
                                onClick = { showDatePicker = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Adicionar Lembrete")
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTaskTitle.isNotBlank()) {
                            viewModel.addTask(newTaskTitle, newTaskDescription, newTaskReminder)
                            newTaskTitle = ""
                            newTaskDescription = ""
                            newTaskReminder = null
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancelar") }
            }
        )

        LaunchedEffect(Unit) {
            delay(100)
            titleFocusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To Today Alarm") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Botão Adicionar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        newTaskTitle = ""
                        newTaskDescription = ""
                        newTaskReminder = null
                        showAddDialog = true
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adicionar")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Barra de Pesquisa
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pesquisar...") },
                trailingIcon = {
                    if (viewModel.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar")
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = "Pesquisar")
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ordenação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Suas Atividades", style = MaterialTheme.typography.titleLarge)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showSortMenu = true }
                ) {
                    Text(
                        text = viewModel.currentSortOption.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "Ordenar")
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.label) },
                                    onClick = {
                                        viewModel.onSortOptionChange(option)
                                        showSortMenu = false
                                    },
                                    trailingIcon = {
                                        if (viewModel.currentSortOption == option) {
                                            Text("✓", color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.filteredTasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggleCompletion = { viewModel.toggleTaskCompletion(task) },
                        onDelete = { viewModel.removeTask(task) },
                        onClick = { onNavigateToDetail(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleCompletion: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) Color.LightGray.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = onToggleCompletion)
            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                    )
                )
                // Exibe ícone de alarme se houver
                if (task.reminderTime != null && !task.isCompleted) {
                    val formatter = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Alarm,
                            contentDescription = "Lembrete",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatter.format(java.util.Date(task.reminderTime)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

// --- Tela de Detalhes ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    taskId: String?,
    viewModel: TodoViewModel,
    onNavigateBack: () -> Unit
) {
    val task = taskId?.let { viewModel.getTaskById(it) }

    var editTitle by remember(task) { mutableStateOf(task?.title ?: "") }
    var editDescription by remember(task) { mutableStateOf(task?.description ?: "") }
    var editReminderTime by remember(task) { mutableStateOf(task?.reminderTime) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showTimePicker = false
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val calendar = Calendar.getInstance()
                        val utcCalendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                        utcCalendar.timeInMillis = selectedDateMillis
                        calendar.set(
                            utcCalendar.get(Calendar.YEAR),
                            utcCalendar.get(Calendar.MONTH),
                            utcCalendar.get(Calendar.DAY_OF_MONTH),
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        calendar.set(Calendar.SECOND, 0)
                        editReminderTime = calendar.timeInMillis
                    }
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") } },
            text = { TimePicker(state = timePickerState) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Tarefa") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (task != null) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )

                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = { editDescription = it },
                        label = { Text("Descrição") },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        maxLines = 5,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )

                    // Seção de Edição do Lembrete
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Alarm, contentDescription = null)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Lembrete", style = MaterialTheme.typography.labelLarge)
                                if (editReminderTime != null) {
                                    val formatter = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault())
                                    Text(formatter.format(java.util.Date(editReminderTime!!)))
                                } else {
                                    Text("Toque para definir", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            if (editReminderTime != null) {
                                IconButton(onClick = { editReminderTime = null }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Remover")
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (task.isCompleted) "Status: Concluída" else "Status: Pendente",
                            color = if (task.isCompleted) Color.Green else MaterialTheme.colorScheme.primary
                        )

                        Button(
                            onClick = {
                                if (editTitle.isNotBlank()) {
                                    viewModel.updateTask(task.id, editTitle, editDescription, editReminderTime)
                                    onNavigateBack()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Salvar")
                        }
                    }
                }
            } else {
                Text("Tarefa não encontrada", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}