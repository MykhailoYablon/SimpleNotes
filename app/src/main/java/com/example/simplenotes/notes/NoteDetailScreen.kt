package com.example.simplenotes.notes


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplenotes.Note
import com.example.simplenotes.color.ColorSelector
import com.example.simplenotes.color.NoteColors
import com.example.simplenotes.getSharedGradient

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
    var selectedColor by remember { mutableStateOf(note.color) }

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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
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
                                        color = selectedColor
                                    )
                                    onUpdate(updatedNote)
                                    isEditing = false
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Check, contentDescription = "Save",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        // Cancel button
                        IconButton(
                            onClick = {
                                isEditing = false
                                // Reset to original values
                                currentTitle = note.title
                                currentMessage = note.message
                            }
                        ) {
                            Icon(
                                Icons.Default.Close, contentDescription = "Cancel",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
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
                            Icon(
                                Icons.Default.Delete, contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color(0xFF553AB7)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = getSharedGradient())
        ) {
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

                    OutlinedTextField(
                        value = currentMessage,
                        onValueChange = { currentMessage = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Message (${currentMessage.length} characters)") },
                        minLines = 10,
                        maxLines = Int.MAX_VALUE,
                        colors = OutlinedTextFieldDefaults.colors(
                            selectionColors = TextSelectionColors(
                                handleColor = MaterialTheme.colorScheme.primary,
                                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Color:", fontWeight = FontWeight.Bold, color = Color(0xFFECECEC))

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NoteColors.colorList.forEach { color ->
                            ColorSelector(
                                color = color,
                                isSelected = selectedColor == color,
                                onSelect = { selectedColor = color }
                            )
                        }
                    }
                } else {
                    // View mode - dark sleek style
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF262626)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        border = CardDefaults.outlinedCardBorder(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(48.dp)
                                    .background(color = NoteColors.colorMap[note.color] ?: NoteColors.Green)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = note.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFECECEC),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                if (currentMessage.isNotEmpty()) {
                                    Text(
                                        text = buildAnnotatedString {
                                            var message = note.message
                                            append(message)
                                        },
                                        color = Color(0xFFB0B0B0),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = java.text.SimpleDateFormat("dd MMM yyyy").format(java.util.Date(note.createdAt)),
                                    fontSize = 12.sp,
                                    color = Color(0xFF8A8A8A)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Note metadata
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2F2F2F)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Note Information",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFECECEC)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row {
                                Icon(
                                    Icons.Default.Create,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFB0B0B0)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${currentMessage.length} characters",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFB0B0B0)
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