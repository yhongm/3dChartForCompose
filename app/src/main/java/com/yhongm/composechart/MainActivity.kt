package com.yhongm.composechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yhongm.composechart.component.ThreeDBarChart
import com.yhongm.composechart.component.ThreeDPieChart
import com.yhongm.composechart.data.ChartData
import com.yhongm.composechart.ui.theme.ComposeChartFor3DTheme
import com.yhongm.composechart.util.toColor


val bars = listOf(
    ChartData(50f, "#4FA2FA".toColor(), "A数据"),
    ChartData(40f, "#68eafd".toColor(), "B数据"),
    ChartData(20f, "#d764fc".toColor(), "C数据"),
    ChartData(100f, "#f7ac46".toColor(), "D数据"),
    ChartData(75f, "#fae94c".toColor(), "E数据")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeChartFor3DTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  Row(modifier = Modifier.padding(innerPadding)) {
                      ThreeDBarChart(
                          modifier = Modifier.fillMaxHeight().width(0.dp).weight(1f),
                          bars = bars,

                          maxHeightRatio = 1f,
                      )

                      ThreeDPieChart(
                          modifier = Modifier.fillMaxHeight().width(0.dp).weight(1f),
                          pies = bars,
                      )
                  }
                }
            }
        }
    }
}
