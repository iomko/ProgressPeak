package com.practice.progress_peak.screens.HabitProgress.OtherComponents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    maxValue: Int,
    currentAmount: Int,
    progressColor: Color = Color.Green,
    outlineColor: Color = Color.LightGray,
    strokeWidth: Dp = 8.dp,
    size: Dp = 100.dp,
    textSize: Float = 14f
) {
    val progress = if (maxValue != 0) currentAmount.toFloat() / maxValue.toFloat() else 0f


    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Draw light gray circular outline
        Canvas(modifier = Modifier.matchParentSize()) {
            val innerRadius = size.toPx() / 2 - strokeWidth.toPx() / 2
            val outerRadius = size.toPx() / 2 + strokeWidth.toPx() / 2
            val center = Offset(size.toPx() / 2, size.toPx() / 2)

            drawCircle(
                color = outlineColor,
                radius = size.toPx() / 2,
                style = Stroke(width = strokeWidth.toPx())
            )
        }

        // Draw progress arc
        Canvas(modifier = Modifier.matchParentSize()) {
            val innerRadius = size.toPx() / 2 - strokeWidth.toPx() / 2
            val outerRadius = size.toPx() / 2 + strokeWidth.toPx() / 2
            val center = Offset(size.toPx() / 2, size.toPx() / 2)

            drawArc(
                color = progressColor,
                startAngle = 270f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )
        }

        // Display text in the center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(progress * 100).toInt()}%",
                textAlign = TextAlign.Center,
                fontSize = textSize.sp
            )
            Text(
                text = "$currentAmount/$maxValue",
                textAlign = TextAlign.Center,
                fontSize = textSize.sp // Use sp extension property
            )
        }
    }
}