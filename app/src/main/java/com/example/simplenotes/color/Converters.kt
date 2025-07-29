package com.example.simplenotes.color

// TypeConverters.kt
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromHighlightRangeList(value: List<HighlightRange>?): String? {
        if (value == null) return null

        return value.joinToString(separator = "|") { highlight ->
            "${highlight.start},${highlight.end},${highlight.color.name}"
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
                        color = HighlightColor.valueOf(parts[2])
                    )
                } catch (e: Exception) {
                    null // Skip invalid entries
                }
            } else null
        }
    }

    @TypeConverter
    fun fromColor(color: androidx.compose.ui.graphics.Color?): Long? {
        return color?.value?.toLong()
    }

    @TypeConverter
    fun toColor(colorValue: Long?): androidx.compose.ui.graphics.Color? {
        return if (colorValue == null) null else androidx.compose.ui.graphics.Color(colorValue.toULong())
    }
}