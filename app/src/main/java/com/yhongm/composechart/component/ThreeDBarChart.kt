package com.yhongm.composechart.component



import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.yhongm.composechart.data.ChartData
import com.yhongm.composechart.util.toColor
import com.yhongm.composechart.util.toPercentage





@Composable
fun ThreeDBarChart(
    bars: List<ChartData>,
    modifier: Modifier = Modifier,
    maxHeightRatio: Float = 0.8f,
    depth: Float = 10f, // 深度值
    strokeWidth: Float = 4f, //X轴Y轴宽度
    strokeColor: Color = "#cae5f0".toColor(), //X轴Y轴颜色
    tickCount: Int = 6, // 刻度数量
    xLableColor: Color = "#525252".toColor(), //X轴文字颜色
    tickLabelColor: Color = "#606162".toColor(),//Y轴文字颜色
    scaleLength: Float = 20f, //刻度长度,
    xLabelTextSize: TextUnit = 16.sp, //X轴文字大小
    tickLabelTextSize: TextUnit = 12.sp, //Y轴文字大小

) {
    val yAxleTextStyle = TextStyle(color = tickLabelColor, fontSize = tickLabelTextSize)
    val xAxleTextStyle =
        TextStyle(color = xLableColor, fontSize = xLabelTextSize, fontWeight = FontWeight.Bold)
    val xAxleTextMeasurer = rememberTextMeasurer()
    val yAxleTextMeasurer = rememberTextMeasurer()
    Canvas(modifier = modifier) {
        var yAxleX = 100f
        var xAxleY = 100f
        var leftPadding = 100f
        var rightPadding = 0f
        var topPadding = 100f
        var bottomPadding = 0f
        val canvasWidth = size.width
        val canvasHeight = size.height
        val barWidth = (canvasWidth - yAxleX - leftPadding) / (bars.size * 2) // 柱体宽度，留出间距
        val maxBarHeight =
            (canvasHeight - topPadding - bottomPadding - xAxleY) * maxHeightRatio // 最大高度

        val tickSpacing = maxBarHeight / (tickCount - 1) // 刻度间距
        bars.forEachIndexed { index, bar ->
            val left = yAxleX + leftPadding + index * barWidth * 2
            val top = canvasHeight - bottomPadding - xAxleY - (bar.value / 100f * (maxBarHeight))
            val right = left + barWidth
            val bottom = canvasHeight - bottomPadding - xAxleY
            val center = (left + right) / 2f
            var topTop = top - depth
            val topBottom = top + depth
            val frontPath = Path().apply {
                moveTo(left, bottom)
                lineTo(left, top)
                lineTo(center, topBottom)
                lineTo(center, bottom)
                close()
            }
            drawPath(frontPath, color = bar.color)
            val topPath = Path().apply {
                moveTo(center, topBottom)
                lineTo(right, top)
                lineTo(right, bottom)
                lineTo(center, bottom)
                close()
            }
            drawPath(topPath, color = bar.color.copy(alpha = 0.8f))
            val sidePath = Path().apply {
                moveTo(left, top)
                lineTo(center, topBottom)
                lineTo(right, top)
                lineTo(center, topTop)
                close()
            }
            drawPath(sidePath, color = bar.color.copy(alpha = 0.6f))
            val layoutResult = xAxleTextMeasurer.measure(bar.label, style = xAxleTextStyle)
            drawText(
                xAxleTextMeasurer,
                bar.label,
                style = xAxleTextStyle,
                topLeft = Offset(center - layoutResult.size.width / 2f, bottom + scaleLength)
            )
            drawLine(
                color = strokeColor,
                start = Offset(center, bottom),
                end = Offset(center, bottom + scaleLength),
                strokeWidth = strokeWidth
            )
        }
        val maxValue = 100f
        val valueStep = maxValue / (tickCount - 1)
        drawLine(
            color = strokeColor,
            start = Offset(yAxleX, 0f + topPadding),
            end = Offset(yAxleX - rightPadding, canvasHeight - bottomPadding - xAxleY),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = strokeColor,
            start = Offset(yAxleX, canvasHeight - bottomPadding - xAxleY),
            end = Offset(canvasWidth - rightPadding, canvasHeight - bottomPadding - xAxleY),
            strokeWidth = strokeWidth
        )

        for (i in 0 until tickCount) {

            val y =
                canvasHeight - bottomPadding - xAxleY - ((i * tickSpacing) / (maxBarHeight)) * (maxBarHeight)
            val tickValue = (i * valueStep).toInt().toPercentage()
            drawLine(
                color = strokeColor,
                start = Offset(yAxleX, y),
                end = Offset(yAxleX - scaleLength, y),
                strokeWidth = strokeWidth
            )
            val textSize = yAxleTextMeasurer.measure(
                tickValue, style = yAxleTextStyle
            )
            drawText(
                textMeasurer = xAxleTextMeasurer,
                text = tickValue.toString(),
                style = yAxleTextStyle,
                topLeft = Offset(
                    yAxleX - scaleLength - textSize.size.width, y - textSize.size.height / 2f
                )
            )

        }
    }
}



