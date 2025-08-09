package com.example.simplenotes.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.simplenotes.color.ColorSelector
import com.example.simplenotes.highlight.HighlightRange
import com.example.simplenotes.highlight.HighlightableTextField
import com.example.simplenotes.color.NoteColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, List<HighlightRange>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("Green") }
    var messageHighlights by remember { mutableStateOf<List<HighlightRange>>(emptyList()) }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Note") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title*") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                HighlightableTextField(
                    value = message,
                    onValueChange = { message = it },
                    highlights = messageHighlights,
                    onHighlightsChange = { messageHighlights = it },
                    label = { Text("Message (${message.length} characters)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, message, selectedColor, messageHighlights)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}