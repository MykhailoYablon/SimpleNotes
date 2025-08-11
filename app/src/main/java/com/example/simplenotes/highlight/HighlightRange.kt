package com.example.simplenotes.highlight

import androidx.compose.ui.graphics.Color

// Data classes for highlighting
data class HighlightRange(
    val start: Int,
    val end: Int,
    val color: Color // Changed from HighlightColor to Color
)

//enum class HighlightColor(val color: Color, val displayName: String) {
//    YELLOW(Color(0xFFFFEB3B), "Yellow"),
//    GREEN(Color(0xFF4CAF50), "Green"),
//    BLUE(Color(0xFF2196F3), "Blue"),
//    PINK(Color(0xFFE91E63), "Pink"),
//    ORANGE(Color(0xFFFF9800), "Orange")
//}