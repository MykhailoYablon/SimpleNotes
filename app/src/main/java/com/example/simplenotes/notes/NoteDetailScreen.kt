package com.example.simplenotes.notes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplenotes.Note
import com.example.simplenotes.highlight.HighlightableTextField
import com.example.simplenotes.color.NoteColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// NoteDetailScreen.kt - Replace your AlertDialog with this full screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note,
    onNavigateBack: () -> Unit,
    onDelete: (Note) -> Unit,
    onUpdate: (Note) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var currentTitle by remember(note.title) { mutableStateOf(note.title) }
    var currentMessage by remember(note.message) { mutableStateOf(note.message) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var currentMessageHighlights by remember(note) {
        mutableStateOf(note.messageHighlights ?: emptyList())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Edit Note" else "Note Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    if (isEditing) {
                        // Save button
                        IconButton(
                            onClick = {
                                if (currentTitle.isNotBlank()) {
                                    val updatedNote = note.copy(
                                        title = currentTitle,
                                        message = currentMessage,
                                        messageHighlights = currentMessageHighlights
                                    )
                                    onUpdate(updatedNote)
                                    isEditing = false
                                }
                            }
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Save",
                                tint = MaterialTheme.colorScheme.onPrimary)
                        }
                        // Cancel button
                        IconButton(
                            onClick = {
                                isEditing = false
                                // Reset to original values
                                currentTitle = note.title
                                currentMessage = note.message
                                currentMessageHighlights = note.messageHighlights ?: emptyList()
                            }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel",
                                tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    } else {
                        // Edit button
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                Icons.Default.Edit, contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        // Delete button
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (isEditing) {
                // Edit mode
                OutlinedTextField(
                    value = currentTitle,
                    onValueChange = { currentTitle = it },
                    label = { Text("Title*") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                HighlightableTextField(
                    value = currentMessage,
                    onValueChange = { currentMessage = it },
                    highlights = currentMessageHighlights,
                    onHighlightsChange = { currentMessageHighlights = it },
                    label = { Text("Message (${currentMessage.length} characters)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 10,
                    maxLines = Int.MAX_VALUE
                )
            } else {
                // View mode

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = NoteColors.backgroundColorMap[note.color]
                            ?: NoteColors.Green
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(
                                        color = NoteColors.colorMap[note.color] ?: NoteColors.Green,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = note.title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (currentMessage.isNotEmpty()) {
                            Text(
                                text = buildAnnotatedString {
                                    var message = note.message
                                    var messageHighlights = note.messageHighlights ?: emptyList()
                                    append(message)
                                    messageHighlights.forEach { highlight ->
                                        val start = highlight.start.coerceAtMost(message.length)
                                        val end = highlight.end.coerceAtMost(message.length)
                                        if (start < end) {
                                            addStyle(
                                                style = SpanStyle(
                                                    background = highlight.color.copy(alpha = 0.3f)
                                                ),
                                                start = start,
                                                end = end
                                            )
                                        }
                                    }
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Note metadata
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Note Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Icon(
                                Icons.Default.Create,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${currentMessage.length} characters",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (currentMessageHighlights.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))

                            Row {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${currentMessageHighlights.size} highlights",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Note") },
            text = { Text("Are you sure you want to delete this note? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(note)
                        showDeleteDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}