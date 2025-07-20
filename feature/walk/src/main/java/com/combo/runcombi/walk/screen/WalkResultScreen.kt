package com.combo.runcombi.walk.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.LottieImage
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.Primary02
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle5
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle6
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.walk.R
import com.combo.runcombi.walk.util.FormatUtils
import com.combo.runcombi.walk.viewmodel.WalkMainViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun WalkResultScreen(
    walkMainViewModel: WalkMainViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
) {
    val walkData = walkMainViewModel.walkData.collectAsState().value
    val formattedTime = FormatUtils.formatMinute(walkData.time)
    val formattedDistance = FormatUtils.formatDistance(walkData.distance)

    WalkResultContent(
        timeText = formattedTime,
        distanceText = formattedDistance,
        pathPoints = walkData.pathPoints,
        onBack = onBack
    )

    DisposableEffect(Unit) {
        onDispose { walkMainViewModel.clearResultData() }
    }
}


@Composable
fun WalkResultContent(
    timeText: String,
    distanceText: String,
    pathPoints: List<LatLng>,
    onBack: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Grey01),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RunCombiAppTopBar(isVisibleBackBtn = false, isVisibleCloseBtn = true, onClose = onBack)
            Spacer(modifier = Modifier.height(22.dp))

            TitleSection()
            Spacer(modifier = Modifier.height(16.dp))

            PathPreview(pathPoints = pathPoints)
            Spacer(modifier = Modifier.height(12.dp))

            StatInfoSection(timeText = timeText, distanceText = distanceText)
            Spacer(modifier = Modifier.weight(1f))

            CameraButton(onClick = { /* TODO */ })
            Spacer(modifier = Modifier.height(44.dp))
        }

        CelebrationEffect()
    }
}


@Composable
fun PathPreview(pathPoints: List<LatLng>) {
    Box(
        modifier = Modifier
            .size(240.dp)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (pathPoints.size > 1) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val xs = pathPoints.map { it.longitude }
                val ys = pathPoints.map { it.latitude }
                val minX = xs.minOrNull() ?: return@Canvas
                val maxX = xs.maxOrNull() ?: return@Canvas
                val minY = ys.minOrNull() ?: return@Canvas
                val maxY = ys.maxOrNull() ?: return@Canvas

                val scaleX = size.width / (maxX - minX).coerceAtLeast(0.0001)
                val scaleY = size.height / (maxY - minY).coerceAtLeast(0.0001)

                val centerX = (xs.maxOrNull()!! + xs.minOrNull()!!) / 2.0
                val centerY = (ys.maxOrNull()!! + ys.minOrNull()!!) / 2.0
                val canvasCenter = Offset(size.width / 2f, size.height / 2f)

                fun toCanvasOffset(point: LatLng): Offset {
                    val x = ((point.longitude - minX) * scaleX).toFloat()
                    val y = size.height - ((point.latitude - minY) * scaleY).toFloat()
                    val pathCenterX = ((centerX - minX) * scaleX).toFloat()
                    val pathCenterY = size.height - ((centerY - minY) * scaleY).toFloat()
                    val offsetX = canvasCenter.x - pathCenterX
                    val offsetY = canvasCenter.y - pathCenterY
                    return Offset(x + offsetX, y + offsetY)
                }

                val path = Path().apply {
                    pathPoints.forEachIndexed { index, point ->
                        val offset = toCanvasOffset(point)
                        if (index == 0) moveTo(offset.x, offset.y) else lineTo(offset.x, offset.y)
                    }
                }

                drawPath(path, color = Primary01, style = Stroke(width = 10f))

                val startOffset = toCanvasOffset(pathPoints.first())
                val endOffset = toCanvasOffset(pathPoints.last())

                drawCircle(Color(0xFFFFFDFD), radius = 14f, center = startOffset)
                drawCircle(Color(0xFFFFFDFD), radius = 14f, center = endOffset)
            }
        }
    }
}


@Composable
fun StatInfoSection(
    timeText: String,
    distanceText: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 70.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = CenterHorizontally) {
            Text("운동 시간", color = Grey06, style = body2)
            Row {
                Text(
                    timeText,
                    color = Grey07,
                    style = giantsTitle2,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(end = 8.dp)
                )
                Text(
                    " min",
                    color = Grey07,
                    style = giantsTitle6,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
        Column(horizontalAlignment = CenterHorizontally) {
            Text("운동 거리", color = Grey06, style = body2)
            Row {
                Text(
                    distanceText,
                    color = Grey07,
                    style = giantsTitle2,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(end = 8.dp)
                )
                Text(
                    " km",
                    color = Grey07,
                    style = giantsTitle6,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
    }
}

@Composable
fun TitleSection() {
    Text("이번 달 7번째 운동", style = giantsTitle1, color = WhiteFF)
    Spacer(modifier = Modifier.height(8.dp))
    Text("앞으로도 계속 함께 할 거죠?", style = giantsTitle5, color = Color(0xEDEDEDE0))
}

@Composable
fun CameraButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(Primary02, shape = RoundedCornerShape(4.dp))
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        StableImage(
            drawableResId = R.drawable.ic_camera_fill,
            modifier = Modifier
                .width(48.dp)
                .height(38.dp)
        )
    }
}

@Composable
fun CelebrationEffect() {
    LottieImage(
        modifier = Modifier.fillMaxSize(),
        lottieRes = R.raw.animation_celebration
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun WalkResultContentPreview() {
    WalkResultContent(
        timeText = "12",
        distanceText = "3.2",
        pathPoints = listOf(
            LatLng(37.5665, 126.9780),
            LatLng(37.5670, 126.9790),
            LatLng(37.5675, 126.9800)
        ),
        onBack = {}
    )
}



