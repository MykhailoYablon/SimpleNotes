package com.example.simplenotes.highlight

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    var showColorMenu by remember { mutableStateOf(false) }
    var hasValidSelection by remember { mutableStateOf(false) }

    // Check if selection is valid and show menu with delay
    LaunchedEffect(textFieldValue.selection) {
        val selection = textFieldValue.selection
        hasValidSelection = selection.start != selection.end &&
                selection.start >= 0 &&
                selection.end <= textFieldValue.text.length

        if (hasValidSelection) {
            // Add delay to allow user to finish selecting text
            kotlinx.coroutines.delay(500) // Wait 500ms
            // Check if selection is still valid after delay
            if (textFieldValue.selection.start != textFieldValue.selection.end) {
                showColorMenu = true
            }
        } else {
            showColorMenu = false
        }
    }

    Column {
        // Text field
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
            visualTransformation = EnhancedHighlightVisualTransformation(highlights, textFieldValue.selection),
            colors = OutlinedTextFieldDefaults.colors(
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                )
            )
        )

        // Color selection menu - outside the text field
        if (showColorMenu && hasValidSelection) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Choose Highlight Color",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(
                            onClick = { showColorMenu = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close menu",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Color options in a grid
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(HighlightColor.entries) { color ->
                            Card(
                                modifier = Modifier
                                    .width(80.dp)
                                    .clickable {
                                        val selection = textFieldValue.selection
                                        val start = minOf(selection.start, selection.end)
                                        val end = maxOf(selection.start, selection.end)

                                        val newRanges = highlights.toMutableList()
                                        newRanges.add(
                                            HighlightRange(
                                                start = start,
                                                end = end,
                                                color = color
                                            )
                                        )
                                        onHighlightsChange(newRanges.sortedBy { it.start })
                                        showColorMenu = false

                                        // Clear selection after adding highlight
                                        textFieldValue = textFieldValue.copy(
                                            selection = TextRange(textFieldValue.selection.end)
                                        )
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = color.color.copy(alpha = 0.2f)
                                ),
                                border = BorderStroke(1.dp, color.color)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                color.color,
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = color.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Clear all button
                    OutlinedButton(
                        onClick = {
                            onHighlightsChange(emptyList())
                            showColorMenu = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear All Highlights")
                    }
                }
            }
        }

        // Display active highlights as chips (more compact)
        if (highlights.isNotEmpty() && showHighlightControls) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(highlights.withIndex().toList()) { (index, highlight) ->
                    val safeStart = highlight.start.coerceAtLeast(0).coerceAtMost(value.length)
                    val safeEnd = highlight.end.coerceAtLeast(safeStart).coerceAtMost(value.length)

                    if (safeStart < safeEnd) {
                        val highlightText = value.substring(safeStart, safeEnd)

                        AssistChip(
                            onClick = {
                                val newHighlights = highlights.toMutableList()
                                newHighlights.removeAt(index)
                                onHighlightsChange(newHighlights)
                            },
                            label = {
                                Text(
                                    text = highlightText.take(15) + if (highlightText.length > 15) "..." else "",
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            },
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            highlight.color.color,
                                            CircleShape
                                        )
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove highlight",
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = highlight.color.color.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier.height(32.dp)
                        )
                    }
                }
            }
        }
    }
}
