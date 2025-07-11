package com.example.simplenotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplenotes.ui.theme.NoteColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailDialog(
    note: Note,
    onDismiss: () -> Unit,
    onDelete: (Note) -> Unit,
    onUpdate: (Note) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(note.title) }
    var message by remember { mutableStateOf(note.message) }
    var selectedColor by remember { mutableStateOf(note.color) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (isEditing) "Edit Note" else "Note Details")

                Row {
                    TextButton(
                        onClick = { isEditing = !isEditing }
                    ) {
                        Text(if (isEditing) "Cancel" else "Edit")
                    }

                    TextButton(
                        onClick = { onDelete(note) }
                    ) {
                        Text("Delete", color = Color.Red)
                    }
                }
            }
        },
        text = {
            if (isEditing) {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title*") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = { if (it.length <= 256) message = it },
                        label = { Text("Message (${message.length}/256)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Color:", fontWeight = FontWeight.Bold)

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
                }
            } else {
                Column {
                    Row(
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

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = note.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = note.message,
                        fontSize = 16.sp
                    )
                }
            }
        },
        confirmButton = {
            if (isEditing) {
                TextButton(
                    onClick = {
                        if (title.isNotBlank()) {
                            onUpdate(
                                note.copy(
                                    title = title,
                                    message = message,
                                    color = selectedColor
                                )
                            )
                        }
                    },
                    enabled = title.isNotBlank()
                ) {
                    Text("Save")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        },
        dismissButton = {
            if (isEditing) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}