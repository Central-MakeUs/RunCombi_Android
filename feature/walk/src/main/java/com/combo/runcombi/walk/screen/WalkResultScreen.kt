package com.combo.runcombi.walk.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.walk.util.FormatUtils
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel

@Composable
fun WalkResultScreen(
    walkRecordViewModel: WalkRecordViewModel = hiltViewModel(),
) {
    val uiState = walkRecordViewModel.uiState.collectAsState().value
    val minutes = uiState.time / 60
    val seconds = uiState.time % 60
    val formattedTime = FormatUtils.formatTime(uiState.time)
    val distanceKm = uiState.distance / 1000.0
    val formattedDistance = FormatUtils.formatDistance(uiState.distance)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(androidx.compose.ui.Alignment.CenterHorizontally)
        ) {
            if (uiState.pathPoints.size > 1) {
                val points = uiState.pathPoints

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val xs = points.map { it.longitude }
                    val ys = points.map { it.latitude }
                    val minX = xs.minOrNull() ?: 0.0
                    val maxX = xs.maxOrNull() ?: 1.0
                    val minY = ys.minOrNull() ?: 0.0
                    val maxY = ys.maxOrNull() ?: 1.0

                    val scaleX = size.width / (maxX - minX).coerceAtLeast(0.0001)
                    val scaleY = size.height / (maxY - minY).coerceAtLeast(0.0001)

                    val path = Path().apply {
                        points.forEachIndexed { i, point ->
                            val x = ((point.longitude - minX) * scaleX).toFloat()
                            val y = size.height - ((point.latitude - minY) * scaleY).toFloat()
                            if (i == 0) moveTo(x, y) else lineTo(x, y)
                        }
                    }

                    val start = points.first()
                    val end = points.last()
                    val startX = ((start.longitude - minX) * scaleX).toFloat()
                    val startY = size.height - ((start.latitude - minY) * scaleY).toFloat()
                    val endX = ((end.longitude - minX) * scaleX).toFloat()
                    val endY = size.height - ((end.latitude - minY) * scaleY).toFloat()

                    drawPath(
                        path,
                        color = Primary01,
                        style = Stroke(width = 12f),
                        alpha = 1f
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 16f,
                        center = androidx.compose.ui.geometry.Offset(startX, startY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 16f,
                        center = androidx.compose.ui.geometry.Offset(endX, endY)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                Text("운동 시간", color = Color.White)
                Text(formattedTime, color = Color.White, modifier = Modifier.padding(top = 4.dp))
            }
            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                Text("운동 거리", color = Color.White)
                Text(
                    "$formattedDistance km",
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
    DisposableEffect(Unit) {
        onDispose {
            walkRecordViewModel.clear()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewWalkResultScreen() {
    WalkResultScreen(
        walkRecordViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    )
} 