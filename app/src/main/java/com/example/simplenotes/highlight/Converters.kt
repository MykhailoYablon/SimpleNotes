package com.example.simplenotes.highlight

// TypeConverters.kt
import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromHighlightRangeList(value: List<HighlightRange>?): String? {
        if (value == null) return null

        return value.joinToString(separator = "|") { highlight ->
            "${highlight.start},${highlight.end},${highlight.color.value}"
        }
    }

    @TypeConverter
    fun toHighlightRangeList(value: String?): List<HighlightRange>? {
        if (value.isNullOrEmpty()) return emptyList()

        return value.split("|").mapNotNull { item ->
            val parts = item.split(",")
            if (parts.size == 3) {
                try {
                    HighlightRange(
                        start = parts[0].toInt(),
                        end = parts[1].toInt(),
                        color = Color(parts[2].removePrefix("#").toLong(16) or 0xFF000000)
                    )
                } catch (e: Exception) {
                    null // Skip invalid entries
                }
            } else null
        }
    }

    @TypeConverter
    fun fromColor(color: Color?): Long? {
        return color?.value?.toLong()
    }

    @TypeConverter
    fun toColor(colorValue: Long?): Color? {
        return if (colorValue == null) null else Color(colorValue.toULong())
    }
}