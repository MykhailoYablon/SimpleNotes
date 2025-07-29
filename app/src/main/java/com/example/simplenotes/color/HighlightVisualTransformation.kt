package com.example.simplenotes.color

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class HighlightVisualTransformation(
    private val highlights: List<HighlightRange>
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val annotatedString = buildAnnotatedString {
            append(text.text)

            // Filter and validate highlights before applying
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
                            background = highlight.color.color.copy(alpha = 0.3f)
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