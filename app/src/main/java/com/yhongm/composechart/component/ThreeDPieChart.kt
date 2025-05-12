package com.yhongm.composechart.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.yhongm.composechart.data.ChartData
import com.yhongm.composechart.util.darken
import com.yhongm.composechart.util.pointAtAngle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


@Composable
fun ThreeDPieChart(
    modifier: Modifier,
    pies: List<ChartData>,
    thickness: Float = 20f, // 饼图厚度
    aspectRatio: Float = 0.5f, // 顶部椭圆的宽高比，模拟倾斜
    showPercentages: Boolean = true, // 是否显示百分比
    showLabels: Boolean = true // 是否显示标签
) {
    Canvas(modifier =modifier) {
        val width = size.width
        val height = size.height


        val w = min(width, height - thickness) * 0.7f
        val h = w * aspectRatio
        val left = (width - w) / 2
        val top = (height - h - thickness) / 2

        val centerX = left + w / 2
        val centerY = top + h / 2


        val topRect = Rect(left, top, left + w, top + h)


        val bottomRect = Rect(left, top + thickness, left + w, top + h + thickness)


        val total = pies.map { it.value }.sum()


        val darkerColors = pies.map { it.color.darken(0.2f) }


        var startAngle = 0f
        for (i in pies.indices) {
            val sweepAngle = (pies[i].value / total) * 360f
            val midpoint = (startAngle + sweepAngle / 2) % 360
            if (midpoint > 270 || midpoint < 90) {
                drawSideFace(startAngle, sweepAngle, topRect, bottomRect, darkerColors[i])
            }

            startAngle += sweepAngle
        }

        startAngle = 0f
        for (i in pies.indices) {
            val sweepAngle = (pies[i].value / total) * 360f
            val midpoint = (startAngle + sweepAngle / 2) % 360  //

            if (midpoint in 90.0..270.0) {
                drawSideFace(startAngle, sweepAngle, topRect, bottomRect, darkerColors[i])
            }

            startAngle += sweepAngle
        }

        startAngle = 0f
        for (i in pies.indices) {
            val sweepAngle = (pies[i].value / total) * 360f
            drawArc(
                color = pies[i].color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(topRect.left, topRect.top),
                size = Size(topRect.width, topRect.height)
            )
            startAngle += sweepAngle
        }

        if (showPercentages || (showLabels)) {
            startAngle = 0f
            for (i in pies.indices) {
                val sweepAngle = (pies[i].value / total) * 360f
                val midAngle = startAngle + (sweepAngle / 2)
                val percentage = (pies[i].value / total * 100).toInt()

                val labelDistance = w * 0.55f
                val labelX = centerX + cos((midAngle * PI / 180).toDouble()).toFloat() * labelDistance
                val labelY = centerY + sin((midAngle * PI / 180).toDouble()).toFloat() * labelDistance * aspectRatio

                val lineStartDistance = w * 0.4f
                val lineStartX = centerX + cos((midAngle * PI / 180).toDouble()).toFloat() * lineStartDistance
                val lineStartY = centerY + sin((midAngle * PI / 180).toDouble()).toFloat() * lineStartDistance * aspectRatio

                drawLine(
                    color = Color.Gray,
                    start = Offset(lineStartX, lineStartY),
                    end = Offset(labelX, labelY),
                    strokeWidth = 1.dp.toPx()
                )

                val text = buildString {
                    if (showLabels && i < pies.size) {
                        append(pies[i].label)
                        if (showPercentages) append(": ")
                    }
                    if (showPercentages) append("$percentage%")
                }

                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 12.dp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText(text, labelX, labelY, paint)
                }

                startAngle += sweepAngle
            }
        }
    }
}

private fun DrawScope.drawSideFace(
    startAngle: Float,
    sweepAngle: Float,
    topRect: Rect,
    bottomRect: Rect,
    color: Color
) {
    val path = Path().apply {
        val startTop = topRect.pointAtAngle(startAngle)
        val startBottom = bottomRect.pointAtAngle(startAngle)
        val endTop = topRect.pointAtAngle(startAngle + sweepAngle)
        val endBottom = bottomRect.pointAtAngle(startAngle + sweepAngle)

        moveTo(startTop.x, startTop.y)
        lineTo(startBottom.x, startBottom.y)
        arcTo(bottomRect, startAngle, sweepAngle, false)
        lineTo(endTop.x, endTop.y)
        arcTo(topRect, startAngle + sweepAngle, -sweepAngle, false)
        close()
    }
    drawPath(path, color = color)
}

