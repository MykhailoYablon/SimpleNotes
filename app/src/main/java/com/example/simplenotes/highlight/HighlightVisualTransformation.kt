package com.example.simplenotes.highlight

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