package com.example.simplenotes.color

import androidx.compose.ui.graphics.Color

object NoteColors {
    // Softer, less bright colors
    val Green = Color(0xFF81C784)      // Light green
    val Blue = Color(0xFF64B5F6)       // Light blue
    val Red = Color(0xFFE57373)        // Light red
    val Yellow = Color(0xFFFFF176)     // Light yellow

    // Even softer background versions for cards
    val GreenBg = Color(0xFFE8F5E8)    // Very light green
    val BlueBg = Color(0xFFE3F2FD)     // Very light blue
    val RedBg = Color(0xFFFFEBEE)      // Very light red
    val YellowBg = Color(0xFFFFFDE7)   // Very light yellow

    val colorMap = mapOf(
        "Green" to Green,
        "Blue" to Blue,
        "Red" to Red,
        "Yellow" to Yellow
    )

    val backgroundColorMap = mapOf(
        "Green" to GreenBg,
        "Blue" to BlueBg,
        "Red" to RedBg,
        "Yellow" to YellowBg
    )

    val colorList = listOf("Green", "Blue", "Red", "Yellow")
}