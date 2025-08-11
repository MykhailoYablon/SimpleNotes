package com.example.simplenotes.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RGBColorPicker(
    onColorSelected: (Color) -> Unit
) {
    val redValue = remember { mutableStateOf(0.5f) }
    val greenValue = remember { mutableStateOf(0.5f) }
    val blueValue = remember { mutableStateOf(0.5f) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Red Slider
        Text("Red")
        Slider(
            value = redValue.value,
            onValueChange = { newValue ->
                redValue.value = newValue
                onColorSelected(Color(redValue.value, greenValue.value, blueValue.value))
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(activeTrackColor = Color.Red)
        )

        // Green Slider
        Text("Green")
        Slider(
            value = greenValue.value,
            onValueChange = { newValue ->
                greenValue.value = newValue
                onColorSelected(Color(redValue.value, greenValue.value, blueValue.value))
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(activeTrackColor = Color.Green)
        )

        // Blue Slider
        Text("Blue")
        Slider(
            value = blueValue.value,
            onValueChange = { newValue ->
                blueValue.value = newValue
                onColorSelected(Color(redValue.value, greenValue.value, blueValue.value))
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(activeTrackColor = Color.Blue)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display selected color
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(redValue.value, greenValue.value, blueValue.value))
        )
    }
}