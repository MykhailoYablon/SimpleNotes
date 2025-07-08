package com.example.simplenotes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesApp(viewModel: NotesViewModel) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())
    val showAddDialog by viewModel.showAddDialog.collectAsState()
    val showDetailDialog by viewModel.showDetailDialog.collectAsState()
    val selectedNote by viewModel.selectedNote.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onClick = { viewModel.showDetailDialog(note) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddNoteDialog(
            onDismiss = { viewModel.hideAddDialog() },
            onSave = { title, message, color ->
                viewModel.addNote(title, message, color)
                viewModel.hideAddDialog()
            }
        )
    }

    if (showDetailDialog && selectedNote != null) {
        NoteDetailDialog(
            note = selectedNote!!,
            onDismiss = { viewModel.hideDetailDialog() },
            onDelete = { note ->
                viewModel.deleteNote(note)
                viewModel.hideDetailDialog()
            },
            onUpdate = { note ->
                viewModel.updateNote(note)
                viewModel.hideDetailDialog()
            }
        )
    }
}
