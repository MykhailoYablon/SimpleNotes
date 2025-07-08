package com.example.simplenotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplenotes.ui.theme.SimpleNotesTheme

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleNotesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NotesApp(noteViewModel)
                }
            }
        }
    }
}

@Composable
fun NotesApp(noteViewModel: NoteViewModel) {
    NotesListScreen(noteViewModel)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimpleNotesTheme {
        // Preview with empty ViewModel or mock data
    }
}