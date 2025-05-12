package com.yhongm.composechart.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun String.toColor(): Color {
    var hex = this.replace("#", "")
    hex = when (hex.length) {
        3 -> "FF${hex[0]}${hex[0]}${hex[1]}${hex[1]}${hex[2]}${hex[2]}"
        6 -> "FF$hex"
        8 -> hex
        else -> throw IllegalArgumentException("Invalid hex format")
    }
    return Color(hex.toLong(16) or 0x00000000FF000000)
}


fun Int.toPercentage(): String {
    return String.format("%.1f", this.toFloat() / 100)
}


fun Rect.pointAtAngle(angle: Float): Offset {
    val cx = (left + right) / 2
    val cy = (top + bottom) / 2
    val a = (right - left) / 2
    val b = (bottom - top) / 2
    val rad = (angle * PI / 180).toDouble()
    return Offset(
        (cx + a * cos(rad)).toFloat(),
        (cy + b * sin(rad)).toFloat()
    )
}

fun Color.darken(fraction: Float): Color {
    return Color(
        red = (red * (1 - fraction)).coerceIn(0f, 1f),
        green = (green * (1 - fraction)).coerceIn(0f, 1f),
        blue = (blue * (1 - fraction)).coerceIn(0f, 1f),
        alpha = alpha
    )
}