package com.example.simplenotes.highlight

// TypeConverters.kt
import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromColor(color: Color?): Long? {
        return color?.value?.toLong()
    }

    @TypeConverter
    fun toColor(colorValue: Long?): Color? {
        return if (colorValue == null) null else Color(colorValue.toULong())
    }
}