package com.example.simplenotes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplenotes.highlight.HighlightRange

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val message: String,
    val color: String,
//    val titleHighlights: List<HighlightRange>? = emptyList(),
    val messageHighlights: List<HighlightRange>? = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)