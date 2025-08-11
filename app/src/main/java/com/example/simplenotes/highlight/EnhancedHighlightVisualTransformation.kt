package com.example.simplenotes.highlight

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

class EnhancedHighlightVisualTransformation(
    private val highlights: List<HighlightRange>,
    private val currentSelection: TextRange
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val annotatedString = buildAnnotatedString {
            append(text.text)

            // Apply existing highlights
            val validHighlights = highlights.filter { highlight ->
                val start = highlight.start.coerceAtLeast(0)
                val end = highlight.end.coerceAtLeast(0)
                start < end && start < text.length && end <= text.length
            }

            validHighlights.forEach { highlight ->
                val start = highlight.start.coerceAtLeast(0).coerceAtMost(text.length)
                val end = highlight.end.coerceAtLeast(start).coerceAtMost(text.length)

                if (start < end) {
                    addStyle(
                        style = SpanStyle(
                            background = highlight.color.copy(alpha = 0.3f)
                        ),
                        start = start,
                        end = end
                    )
                }
            }

            // Enhance current selection with larger text and border
            if (currentSelection.start != currentSelection.end) {
                val start = currentSelection.start.coerceAtLeast(0).coerceAtMost(text.length)
                val end = currentSelection.end.coerceAtLeast(start).coerceAtMost(text.length)

                if (start < end) {
                    addStyle(
                        style = SpanStyle(
                            fontSize = 18.sp, // Make selected text bigger
                            fontWeight = FontWeight.Bold,
                            background = Color(0xFF2196F3).copy(alpha = 0.2f),
                            textDecoration = TextDecoration.Underline
                        ),
                        start = start,
                        end = end
                    )
                }
            }
        }

        return TransformedText(annotatedString, OffsetMapping.Identity)
    }
}

// Helper function to adjust highlights when text changes
fun adjustHighlightsForTextChange(
    highlights: List<HighlightRange>,
    oldText: String,
    newText: String
): List<HighlightRange> {
    if (oldText == newText) return highlights

    // Filter and fix invalid highlights
    return highlights.mapNotNull { highlight ->
        val start = highlight.start.coerceAtLeast(0).coerceAtMost(newText.length)
        val end = highlight.end.coerceAtLeast(0).coerceAtMost(newText.length)

        // Only keep highlights that have valid ranges
        if (start < end && start < newText.length) {
            highlight.copy(start = start, end = end)
        } else {
            null // Remove invalid highlights
        }
    }
}