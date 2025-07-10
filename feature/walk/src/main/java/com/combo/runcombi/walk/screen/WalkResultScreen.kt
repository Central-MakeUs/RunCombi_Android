package com.combo.runcombi.walk.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel

@Composable
fun WalkResultScreen(
    walkRecordViewModel: WalkRecordViewModel = hiltViewModel(),
) {
    val uiModel = walkRecordViewModel.uiModel.collectAsState().value
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)) {
        Text("운동 시간: ${uiModel.time}초")
        Text("운동 거리: ${uiModel.distance} m")
        Text("평균 속도: ${uiModel.speed} m/s")
        Text("칼로리 소모: ${String.format("%.1f", uiModel.calorie)} kcal")
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            if (uiModel.pathPoints.size > 1) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val xs = uiModel.pathPoints.map { it.longitude }
                    val ys = uiModel.pathPoints.map { it.latitude }
                    val minX = xs.minOrNull() ?: 0.0
                    val maxX = xs.maxOrNull() ?: 1.0
                    val minY = ys.minOrNull() ?: 0.0
                    val maxY = ys.maxOrNull() ?: 1.0
                    val scaleX = size.width / (maxX - minX).coerceAtLeast(0.0001)
                    val scaleY = size.height / (maxY - minY).coerceAtLeast(0.0001)
                    val path = Path().apply {
                        uiModel.pathPoints.forEachIndexed { i, point ->
                            val x = ((point.longitude - minX) * scaleX).toFloat()
                            val y = size.height - ((point.latitude - minY) * scaleY).toFloat()
                            if (i == 0) moveTo(x, y) else lineTo(x, y)
                        }
                    }
                    drawPath(
                        path,
                        color = Color.Red,
                        style = Stroke(width = 6f)
                    )
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            walkRecordViewModel.clear()
        }
    }
} 