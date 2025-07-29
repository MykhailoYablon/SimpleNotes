package com.example.simplenotes.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Enhanced OutlinedTextField with highlighting support
@Composable
fun HighlightableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    highlights: List<HighlightRange>,
    onHighlightsChange: (List<HighlightRange>) -> Unit,
    label: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    showHighlightControls: Boolean = true
) {
    var textFieldValue by remember(value) {
        mutableStateOf(TextFieldValue(value, TextRange(value.length)))
    }
    var showColorPicker by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(HighlightColor.YELLOW) }

    Column {
        // Highlight controls
        if (showHighlightControls) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showColorPicker = !showColorPicker },
                    colors = ButtonDefaults.buttonColors(containerColor = selectedColor.color),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Highlight",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        val selection = textFieldValue.selection
                        if (selection.start != selection.end && selection.start >= 0 && selection.end <= textFieldValue.text.length) {
                            val start = minOf(selection.start, selection.end)
                            val end = maxOf(selection.start, selection.end)

                            val newRanges = highlights.toMutableList()
                            newRanges.add(
                                HighlightRange(
                                    start = start,
                                    end = end,
                                    color = selectedColor
                                )
                            )
                            onHighlightsChange(newRanges.sortedBy { it.start })
                        }
                    },
                    enabled = textFieldValue.selection.let {
                        it.start != it.end && it.start >= 0 && it.end <= textFieldValue.text.length
                    },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Add", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        onHighlightsChange(emptyList())
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Clear All", color = Color.White, fontSize = 12.sp)
                }
            }

            // Color picker
            if (showColorPicker) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(HighlightColor.entries) { color ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color.color,
                                    CircleShape
                                )
                                .border(
                                    width = if (selectedColor == color) 3.dp else 1.dp,
                                    color = if (selectedColor == color) Color.Black else Color.Gray,
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedColor = color
                                    showColorPicker = false
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Text field with highlighting
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                onValueChange(newValue.text)

                // Adjust highlights when text changes
                val adjustedHighlights = adjustHighlightsForTextChange(
                    highlights,
                    value,
                    newValue.text
                )
                if (adjustedHighlights != highlights) {
                    onHighlightsChange(adjustedHighlights)
                }
            },
            label = label,
            modifier = modifier,
            minLines = minLines,
            maxLines = maxLines,
            visualTransformation = HighlightVisualTransformation(highlights)
        )

        // Display active highlights
        if (highlights.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(highlights.withIndex().toList()) { (index, highlight) ->
                    val safeStart = highlight.start.coerceAtLeast(0).coerceAtMost(value.length)
                    val safeEnd = highlight.end.coerceAtLeast(safeStart).coerceAtMost(value.length)

                    if (safeStart < safeEnd) {
                        val highlightText = value.substring(safeStart, safeEnd)

                        Surface(
                            color = highlight.color.color.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.clickable {
                                val newHighlights = highlights.toMutableList()
                                newHighlights.removeAt(index)
                                onHighlightsChange(newHighlights)
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = highlightText.take(20) + if (highlightText.length > 20) "..." else "",
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove highlight",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}