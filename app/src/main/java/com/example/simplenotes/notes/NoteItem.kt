package com.example.simplenotes.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplenotes.Note
import com.example.simplenotes.color.NoteColors
import androidx.compose.ui.platform.LocalDensity

@Composable

fun NoteItem(
    note: Note,
    isStraight: Boolean,
    onClick: () -> Unit
) {
    val targetRotation = if (isStraight) 0f else 30f
    val animatedRotation by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(durationMillis = 220),
        label = "tilt"
    )
    val cameraDistancePx = with(LocalDensity.current) { 16.dp.toPx() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(rotationX = animatedRotation, cameraDistance = cameraDistancePx)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF262626)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left color strip for categorization
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .background(color = NoteColors.colorMap[note.color] ?: NoteColors.Green)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFFECECEC)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = buildAnnotatedString {
                        var message = note.message
                        append(message)
                    },
                    fontSize = 14.sp,
                    color = Color(0xFFB0B0B0),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = java.text.SimpleDateFormat("dd MMM yyyy").format(java.util.Date(note.createdAt)),
                    fontSize = 12.sp,
                    color = Color(0xFF8A8A8A)
                )
            }
        }
    }
}