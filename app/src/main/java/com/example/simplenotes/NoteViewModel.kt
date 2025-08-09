package com.example.simplenotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.dao.NoteRepository
import com.example.simplenotes.highlight.HighlightRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _showDetailScreen = MutableStateFlow(false)
    val showDetailScreen = _showDetailScreen.asStateFlow()

    val notes = repository.getAllNotes()

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote = _selectedNote.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    fun showAddDialog() {
        _showAddDialog.value = true
    }

    fun hideAddDialog() {
        _showAddDialog.value = false
    }

    fun showDetailDialog(note: Note) {
        _selectedNote.value = note
        _showDetailScreen.value = true
    }

    fun hideDetailScreen() {
        _showDetailScreen.value = false
        _selectedNote.value = null
    }

    fun addNote(title: String, message: String, color: String, highlights: List<HighlightRange>) {
        viewModelScope.launch {
            repository.insertNote(
                Note(
                    title = title,
                    message = message,
                    color = color,
                    messageHighlights = highlights
                )
            )
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}