package com.example.simplenotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

    var straightNoteId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
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
                    containerColor = Color.Transparent,
                    titleContentColor = Color(0xFF553AB7)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = getSharedGradient())
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        isStraight = straightNoteId == note.id,
                        onClick = {
                            if (straightNoteId != note.id) {
                                straightNoteId = note.id
                            } else {
                                viewModel.showDetailDialog(note)
                            }
                        }
                    )
                }
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


@Composable
fun getSharedGradient(): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            Color(0xFF282272), // Blue
            Color(0xFF553AB7), // Purple
            Color(0x4D6C43FF)
        )
    )
}