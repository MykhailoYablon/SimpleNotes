package com.example.simplenotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.simplenotes.notes.AddNoteDialog
import com.example.simplenotes.notes.NoteDetailScreen
import com.example.simplenotes.notes.NoteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesApp(viewModel: NotesViewModel) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())
    val showAddDialog by viewModel.showAddDialog.collectAsState()
    val showDetailScreen by viewModel.showDetailScreen.collectAsState()
    val selectedNote by viewModel.selectedNote.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Simple Notes",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                containerColor = Color(0xFF6200EE)
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
            onSave = { title, message, color, highlights ->
                viewModel.addNote(title, message, color, highlights)
                viewModel.hideAddDialog()
            }
        )
    }

    if (showDetailScreen && selectedNote != null) {
        NoteDetailScreen(
            note = selectedNote!!,
            onNavigateBack = { viewModel.hideDetailScreen() },
            onDelete = { note ->
                viewModel.deleteNote(note)
                viewModel.hideDetailScreen()
            },
            onUpdate = { note ->
                viewModel.updateNote(note)
                viewModel.hideDetailScreen()
            }
        )
    }
}
