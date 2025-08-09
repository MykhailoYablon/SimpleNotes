package com.example.simplenotes.color

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorSelector(
    color: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = NoteColors.colorMap[color] ?: NoteColors.Green,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}